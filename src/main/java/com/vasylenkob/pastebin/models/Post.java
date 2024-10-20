package com.vasylenkob.pastebin.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Post {
 private final String postTitle;
 private final String content;
 private final int lifetimeMinutes;
}
