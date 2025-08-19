package com.khoi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JWTProvider {
    /**
        Được đánh dấu @Service: để Spring quản lý như một Bean.
        Là nơi tạo ra JWT khi user login thành công.
        Cũng là nơi giải mã JWT để lấy thông tin như email, role v.v.

        Tổng kết :
            generateToken()	                Tạo JWT chứa thông tin email và quyền
            getEmailFromJwtToken(token)	    Giải mã JWT, lấy email (cần sửa để return email)
            populateAuthorities()	        Chuyển danh sách role thành chuỗi
            SecretKey key	                Key dùng để ký và xác minh JWT (HS256)
    **/

    // Tạo khóa bí mật từ chuỗi SECRET_KEY (chuỗi này nên dài ≥32 ký tự nếu dùng HS256)
    // Dùng cho cả chữ ký và xác minh JWT
    SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());

    public String generateToken(Authentication authentication) {
        /**
            Hàm này tạo JWT mới sau khi người dùng đăng nhập thành công
        **/
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = populateAuthorities(authorities); // Gọi hàm tự viết populateAuthorities() để gộp lại thành chuỗi "ROLE_USER,ROLE_ADMIN"

        return Jwts
                .builder()                                              // Tạo một builder để bắt đầu cấu hình nội dung JWT
                .issuedAt(new Date())                                   // Gán thời điểm token được phát hành (giờ hiện tại), Thuộc tính này sẽ nằm trong phần "iat" (issued at) của token
                .expiration(new Date(new Date().getTime() + 86400000))  // Gán thời điểm hết hạn của token (ở đây: hiện tại + 86,400,000 ms = 24 giờ), Đây là phần "exp" trong token
                .claim("email", authentication.getName())           // "email" : email của người dùng (dựa trên authentication.getName())
                .claim("authorities", role)                         // "authorities" : "ROLE_USER,ROLE_ADMIN"
                .signWith(key, Jwts.SIG.HS256)                         // Ký token bằng key bí mật, HS256 là thuật toán ký phổ biến dùng với SecretKey
                .compact();                                            // Tạo ra chuỗi JWT hoàn chỉnh, nghĩa là dùng để "build" JWT → trả về chuỗi token hoàn chỉnh (gồm 3 phần: header, payload, signature)

    }

    public String getEmailFromJwtToken(String token) {
        /**
            Hàm này giải mã JWT → Lấy ra email user
         */
        token = token.substring(7); // Cắt bỏ tiền tố "Bearer " trong header Authorization

        Claims claims = Jwts
                .parser()                   // giải mã
                .verifyWith(key)            // Chỉ định khóa bí mật để xác minh chữ ký của JWT token, key là SecretKey, tạo từ secret string
                .build()                    // Dùng để hoàn tất cấu hình parser và tạo ra một JwtParser
                .parseSignedClaims(token)   // Nếu token bị sai chữ ký, hết hạn, hoặc không hợp lệ → ném ra lỗi tương ứng
                .getPayload();              // Trả về phần payload chứa Claims (dữ liệu bên trong JWT)

        // Lấy thông tin user từ token
        // Lấy email + authorities từ claim
        String email = String.valueOf(claims.get("email"));

        return email;
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        // Nhận danh sách role của user
        Set<String> auth = new HashSet<>();

        for(GrantedAuthority authority: authorities) {
            auth.add(authority.getAuthority());
        }

        // Biến thành chuỗi ngăn cách bằng dấu , (ví dụ : "ROLE_USER,ROLE_ADMIN")
        return String.join(",", auth);
    }
}
