package lk.ac.iit.eventticketingbackend;

public class Cli {
    public static void main(String[] args) {
        System.out.println("Running standalone task...");

        StringBuilder sb = new StringBuilder();

        sb.append("Hello");
        sb.append(" World");

        String str = sb.toString();

        System.out.println(str);

        System.out.println(str.concat("!"));
    }

}


