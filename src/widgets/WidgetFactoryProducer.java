package widgets;

public final class WidgetFactoryProducer {

    private WidgetFactoryProducer(){
        throw new AssertionError();
    }

    public static WidgetFactory getFactory(String environment){

        //根據Environment參數決定要Return哪個ConcreteFactory
        switch(environment) {
            case "Win": return new WinWidgetFactory();
            case "Linux": return new LinuxWidgetFactory();
        }

        return null;
    }

}