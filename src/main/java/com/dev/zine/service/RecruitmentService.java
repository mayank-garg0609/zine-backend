package com.dev.zine.service;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.recruitment.RecruitmentCreateBody;
import com.dev.zine.dao.RecruitmentDAO;
import com.dev.zine.exceptions.RecruitmentNotFound;
import com.dev.zine.exceptions.StageAlreadyExists;
import com.dev.zine.model.Recruitment;
import com.dev.zine.utils.NullAwareBeanUtilsBean;

import java.util.List;

@Service
public class RecruitmentService {
    @Autowired
    private RecruitmentDAO recruitmentDAO;

    public Recruitment createRecruitment(RecruitmentCreateBody body) throws StageAlreadyExists{
        if(recruitmentDAO.existsByStage(body.getStage())){
            throw new StageAlreadyExists(body.getStage());
        }
        Recruitment newR = new Recruitment();
        newR.setDescription(body.getDescription());
        newR.setTitle(body.getTitle());
        newR.setStage(body.getStage());
        recruitmentDAO.save(newR);
        return newR;
    }

    public Recruitment getRecruitment(Long id) throws RecruitmentNotFound{
        Recruitment r = recruitmentDAO.findById(id).orElseThrow(() -> new RecruitmentNotFound(id));
        return r;
    }

    public List<Recruitment> getAllRecruitments() {
        return recruitmentDAO.findAll();
    }

    public Recruitment editRecruitment(Long id, RecruitmentCreateBody update) throws RecruitmentNotFound{
        Recruitment existing = recruitmentDAO.findById(id).orElseThrow(() -> new RecruitmentNotFound(id));
        try{
            NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            beanUtilsBean.copyProperties(existing, update);
            recruitmentDAO.save(existing);
            return existing;
        } catch(IllegalAccessException | InvocationTargetException e){
            return existing;
        }
    }

    public boolean deleteRecruitment(List<Long> ids){
        try{
            recruitmentDAO.deleteAllById(ids);
            return true;
        } catch(Exception e){
            throw e;
        }
    }
}
