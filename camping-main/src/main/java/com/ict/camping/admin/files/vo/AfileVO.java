package com.ict.camping.admin.files.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfileVO {
    private int file_idx;
    private String file_name;
    private String file_path;
    private int file_size;
    private String file_type;
    private String type;
    private String created_at;
}
