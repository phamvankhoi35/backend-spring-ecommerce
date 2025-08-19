package com.khoi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private int mrpPrice; // mức giá bán lẻ cao nhất, giá khi chưa sale
    private int sellingPrice; // giá thực tế, giá niêm yết
    private int discountPercent;
    private int quantity;

    private String color;

    @ElementCollection // dùng để ánh xạ một tập hợp các kiểu giá trị đơn giản hoặc các đối tượng có thể nhúng (embedded) vào một bảng riêng biệt trong cơ sở dữ liệu
    private List<String> image = new ArrayList<>();

    private int numRating;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Seller seller;

    private LocalDateTime createAt;

    private String size;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
