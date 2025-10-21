public class test {
    public static void main(String[] args) {
        String dec = new String(java.util.Base64.getDecoder().decode("Z2F5"));
        System.out.println(dec);
    }
}