public class ServerCtrl {
    public static void main(String[] args){

        SOENServer soenServer = SOENServer.getDepartment();
        INSEServer inseServer = INSEServer.getDepartment();
        COMPServer compServer = COMPServer.getDepartment();

        try{

            soenServer.init(5050);
            soenServer.registerUser("SOENA1111");
            soenServer.registerUser("SOENS1111");
            Runnable soenRecTask = ()->{
                soenServer.receive();
            };

            inseServer.init(6060);
            inseServer.registerUser("INSEA1111");
            inseServer.registerUser("INSES1111");
            Runnable inseRecTask = ()->{
                inseServer.receive();
            };

            compServer.init(7070);
            compServer.registerUser("COMPA1111");
            compServer.registerUser("COMPS1111");
            Runnable compRecTask = ()->{
                compServer.receive();
            };

        }catch(Exception e){
            System.err.println(e.getMessage());
        }

    }
}
