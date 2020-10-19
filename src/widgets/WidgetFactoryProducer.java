package widgets;

public final class WidgetFactoryProducer {

    private WidgetFactoryProducer(){
        throw new AssertionError();
    }

    public static WidgetFactory getFactory(String environment){

        switch(environment) {
            case "Win": return new WinWidgetFactory();
            case "Linux": return new LinuxWidgetFactory();
        }

        return null;
    }

}