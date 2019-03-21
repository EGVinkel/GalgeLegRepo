package brugerautorisation;

public class UniqueKegGen {


    public static String getKey() {


        String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";


        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {

            int index
                    = (int) (key.length()
                    * Math.random());

            sb.append(key
                    .charAt(index));
        }

        return sb.toString();
    }


}