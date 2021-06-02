package io.github.sudharsan_selvaraj.e2e.admin_panel.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Integer userId;
    private Integer id;
    private String title;
    private String body;

}
