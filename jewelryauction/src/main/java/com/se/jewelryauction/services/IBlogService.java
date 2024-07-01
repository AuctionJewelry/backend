package com.se.jewelryauction.services;

import com.se.jewelryauction.models.BlogEntity;
import com.se.jewelryauction.requests.BlogRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBlogService {
    BlogEntity createBlog(BlogRequest blogRequest, List<MultipartFile> images) throws IOException;

    void deleteBlog(Long blogId);

    List<BlogEntity> getAllBlogs();

    BlogEntity getBlogById(long id);
}
