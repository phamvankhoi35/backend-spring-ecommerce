package com.khoi.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    /**
        - Lớp này là 1 custom filter chạy trước mỗi request HTTP
        - OncePerRequestFilter : được sử dụng để đảm bảo rằng token chỉ được xác
        thực một lần duy nhất cho mỗi yêu cầu, ngay cả khi yêu cầu đó được chuyển
        tiếp hoặc bao gồm nhiều lần.
        - Kiểm tra token JWT trong mỗi request gửi đến Spring Boot
        xem có hợp lệ ko

        Tổng kết :
        1    Authorization header     Nhận JWT từ request
        2    JwtTokenValidator	      Filter để xác minh token mỗi request
        3    parseClaimsJws()	      Giải mã + xác minh chữ ký token
        4    SecurityContextHolder	  Gắn user vào context để controller có thể @AuthenticationPrincipal
        5    AuthorityUtils	          Convert chuỗi quyền thành danh sách Spring roles
    **/

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // (1) Lấy token từ header http, thường là : Authorization: Bearer <token>
        String header = request.getHeader("Authorization");

        // (2) Nếu có token, xử lý tiếp
        if(header != null) {
            // Loại bỏ 7 kí tự "Bearer " để lấy token thô (eyJhbGciOi...)
            String token = header.substring(7);

            try {
                // (3) Giải mã token (parse JWT) + xác minh token
                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
                Claims claims = Jwts
                        .parser()                   // giải mã payload
                        .verifyWith(key)            // Chỉ định khóa bí mật để xác minh chữ ký của JWT token, key là SecretKey, tạo từ secret string
                        .build()                    // Dùng để hoàn tất cấu hình parser và tạo ra một JwtParser
                        .parseSignedClaims(token)   // Nếu token bị sai chữ ký, hết hạn, hoặc không hợp lệ → ném ra lỗi tương ứng
                        .getPayload();              // Trả về phần payload chứa Claims (dữ liệu bên trong JWT)

                // (4) Lấy thông tin user từ token
                String email = String.valueOf(claims.get("email")); // Lấy email + authorities từ claim
                String authorities = String.valueOf(claims.get("authorities")); // authorities thường là chuỗi ROLE_CUSTOMER, ROLE_ADMIN...

                // (5) Tạo Authentication object và gán vào SecurityContext
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Tạo UsernamePasswordAuthenticationToken chứa user + role
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                // Đặt authentication vào SecurityContextHolder để Spring biết request này là đã xác thực và đăng nhập hợp lệ
                SecurityContextHolder.getContext().setAuthentication(authentication);

            // (6) Xử lý lỗi (token không hợp lệ)
            } catch (ExpiredJwtException e) {
                throw new BadCredentialsException("Token hết hạn", e);
            } catch (UnsupportedJwtException e) {
                throw new BadCredentialsException("Token không hỗ trợ", e);
            } catch (MalformedJwtException e) {
                throw new BadCredentialsException("Token sai định dạng", e);
            } catch (SecurityException | io.jsonwebtoken.security.SignatureException e) {
                throw new BadCredentialsException("Chữ ký token không hợp lệ", e);
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Token rỗng hoặc không hợp lệ", e);
            }
        }

        // Cho request tiếp tục
        // Gọi tiếp filter kế tiếp trong chuỗi, hoặc controller nếu không còn filter.
        filterChain.doFilter(request, response);
    }
}
