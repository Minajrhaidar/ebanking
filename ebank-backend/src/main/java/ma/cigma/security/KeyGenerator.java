//package ma.cigma.security;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import java.util.Base64;
//
//public class KeyGenerator {
//  public static void main(String[] args) {
//    byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
//    String base64Key = Base64.getEncoder().encodeToString(keyBytes);
//    System.out.println(base64Key);
//  }
//}
