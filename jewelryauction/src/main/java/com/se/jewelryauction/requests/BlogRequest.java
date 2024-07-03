package com.se.jewelryauction.requests;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {
    private String title;

    private String content;

}
