package com.ict.camping.admin.notices.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APopupVO {
    private int popup_idx;
    private String popup_content;
    private int admin_idx;
    private int height;
    private int width;
    private int left_space;
    private int top_space;
    private String created_at;
    private String is_hidden;
    private String popup_name;

    // join
    private int file_idx;
    private String file_name;
    private String file_path;
    private int file_size;
    private String file_type;
    private String type;
    private String id;
}
