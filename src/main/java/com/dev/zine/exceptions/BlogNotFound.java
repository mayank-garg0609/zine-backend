package com.dev.zine.exceptions;

public class BlogNotFound extends Exception{
    public BlogNotFound(Long id){
        super("Blog "+id.toString()+" not found");
    }
}
