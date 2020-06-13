package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * @author：lanjy
 * @date：2020/6/11
 * @description：注解@NoRepositoryBean
 * 在做项目时创建对象的功能会交给Spring去管理在扫描Reposytory层包时会扫描到BaseReposytory接口 ;
 * 所有对象类接口都会继承此接口 为了告诉JPA不要创建对应接口的bean对象 就在类上加注解@NoRepositoryBean
 * 这样spring容器中就不会有BaseReposytory接口的bean对象
 */
@NoRepositoryBean
public interface  BaseRepository<T, I extends Serializable> extends PagingAndSortingRepository<T, I>, JpaSpecificationExecutor<T> {
}
