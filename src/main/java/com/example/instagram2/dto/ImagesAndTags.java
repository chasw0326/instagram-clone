package com.example.instagram2.dto;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImagesAndTags {

    Image images;

    List<Tag> tags;
}
