package utils;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class HTMLtoOOSEFILEParser implements HTMLParser<String>{

    @Override
    public String parse(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();
        body.insertChildren(body.childNodeSize()-2, new Element(Tag.valueOf("p"), ""));
        return getParse(body);
    }

    public String getParse(Element elem){
        //若元素為空，回傳空
        if(elem == null) return "";

        String span_string = isDecorator(elem.tagName())? getDecorateeString(elem): null;
        String oosefile_string =
                "{\"tag\":\"" + elem.tagName() + "\"," +
                "\"attribute\":\"" +elem.attributes().toString().replace("\\", "\\\\").replace("\"", "\\\"").replaceAll(" style=\\\\?[\\\"'].*\\\\?['\\\"]", "") + "\"," +
                "\"content\":\"" +elem.ownText().replace("\"", "\\\"") + "\"," +
                "\"decoratee\": " + span_string ;

        //插入子元素
        if(!(elem.tagName().equals("character") || isDecorator(elem.tagName()))) {
            String child_string = getChildListParse(elem.childNodes());
            oosefile_string += ",\"child\":[" + child_string + "]";
        }
        oosefile_string += "}";

        return oosefile_string;
    }

    public String getChildListParse(List<Node> elems){
        ArrayList<String> oosefile_list = new ArrayList<>();
        String oosefile_string = "";
        for (Node node : elems) {
            if(node instanceof TextNode){
                for (String s: preProcessedText(((TextNode) node).toString()).split("")) {
                    if(!((TextNode) node).isBlank()) {
                        Element textElement = new Element(Tag.valueOf("character"), "");
                        if(s.equals(" "))  s = "&nbsp;";
                        textElement.text(s);
                        oosefile_list.add(getParse(textElement));
                    }
                }
            }else {
                String g = getParse((Element) node);
                if (g != "") oosefile_list.add(g);
            }
        }
        oosefile_string = String.join(",", oosefile_list);
        return oosefile_string;
    }

    public String getDecorateeString(Element elem){
        String span_string =
                "{\"tag\":\"span\"," +
                "\"attribute\":\"\"," +
                "\"content\":\"\"," +
                "\"decoratee\":\"\"," +
                "\"child\":[" + getChildListParse(elem.childNodes()) + "]}";

        return span_string;
    }

    public String preProcessedText(String input){
        return input.replaceAll(" ", "").replaceAll("(&nbsp;)|(&#160;)|(&amp;nbsp;)", " ");
    }

    public boolean isDecorator(String tagName){
        List<String> normalFactory = List.of("span", "p", "img", "body", "character");
        List<String> decoratorFactory = List.of("b", "i", "u", "font");
        return decoratorFactory.contains(tagName);
    }
}