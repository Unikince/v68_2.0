package com.dmg.gameconfigserver.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.user.GoodNumberDao;
import com.dmg.gameconfigserver.dao.user.UserDao;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.bean.user.GoodNumberBean;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.dto.user.GoodNumberPageDTO;
import com.dmg.gameconfigserver.model.vo.user.GoodNumberPageVO;
import com.dmg.gameconfigserver.service.sys.SysUserService;
import com.dmg.gameconfigserver.service.user.GoodNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("goodNumberService")
public class GoodNumberServiceImpl implements GoodNumberService {

    @Autowired
    private GoodNumberDao goodNumberDao;
    @Autowired
    private SysUserService sysUserDao;
    @Autowired
    private UserDao userDao;

    @Override
    public IPage<GoodNumberPageDTO> getPage(GoodNumberPageVO goodNumberPageVO) {
        IPage<GoodNumberPageDTO> goodNumberPageDTOIPage = new Page<>();
        IPage<GoodNumberBean> goodNumberBeanIPage = goodNumberDao.selectPage(goodNumberPageVO.getPageCondition(),new LambdaQueryWrapper<GoodNumberBean>()
                .eq(goodNumberPageVO.getUserId()!=null,GoodNumberBean::getUserId,goodNumberPageVO.getUserId())
                .eq(goodNumberPageVO.getGoodNumber()!=null,GoodNumberBean::getGoodNumber,goodNumberPageVO.getGoodNumber())
                .orderByDesc(GoodNumberBean::getUpdateDate));
        BeanUtils.copyProperties(goodNumberBeanIPage,goodNumberPageDTOIPage);
        if (CollectionUtils.isEmpty(goodNumberBeanIPage.getRecords())){
            return goodNumberPageDTOIPage;
        }
        List<GoodNumberPageDTO> goodNumberPageDTOS = new ArrayList<>();
        goodNumberBeanIPage.getRecords().forEach(goodNumberBean -> {
            GoodNumberPageDTO goodNumberPageDTO = new GoodNumberPageDTO();
            BeanUtils.copyProperties(goodNumberBean,goodNumberPageDTO);
            SysUserBean sysUserBean = sysUserDao.getUserInfoById(goodNumberBean.getUpdateUserId());
            goodNumberPageDTO.setUpdateUserName(sysUserBean.getUserName());
            UserBean userBean = userDao.selectById(goodNumberBean.getUserId());
            goodNumberPageDTO.setNickname(userBean.getUserName());
            goodNumberPageDTOS.add(goodNumberPageDTO);
        });
        goodNumberPageDTOIPage.setRecords(goodNumberPageDTOS);
        return goodNumberPageDTOIPage;
    }

    @Override
    public void distribute(Long goodNumber, Long userId, Long operatorId) {
        UserBean userBean = userDao.selectById(userId);
        if (userBean == null){
            throw new BusinessException(ResultEnum.USER_NO_EXIST.getCodeStr(),ResultEnum.USER_NO_EXIST.getMsg());
        }
        //检查用户是否已绑定靓号
        GoodNumberBean goodNumberBean = goodNumberDao.selectOne(new LambdaQueryWrapper<GoodNumberBean>().eq(GoodNumberBean::getUserId,userId));
        if (goodNumberBean!=null){
            throw new BusinessException(ResultEnum.PLAYER_HAS_BE_DISTRIBUTE.getCodeStr(),ResultEnum.PLAYER_HAS_BE_DISTRIBUTE.getMsg());
        }
        goodNumberBean = goodNumberDao.selectOne(new LambdaQueryWrapper<GoodNumberBean>().eq(GoodNumberBean::getGoodNumber,goodNumber));
        //检查当前靓号是否已被使用
        if (goodNumberBean!=null && goodNumberBean.getUserId()!=null && !userId.equals(goodNumberBean.getUserId())){
            throw new BusinessException(ResultEnum.GOOD_NUM_HAS_BE_DISTRIBUTE.getCodeStr(),ResultEnum.GOOD_NUM_HAS_BE_DISTRIBUTE.getMsg());
        }
        goodNumberBean = new GoodNumberBean();
        goodNumberBean.setUserId(userId);
        goodNumberBean.setGoodNumber(goodNumber);
        goodNumberBean.setCreateUserId(operatorId);
        goodNumberBean.setCreateDate(new Date());
        goodNumberBean.setUpdateDate(new Date());
        goodNumberBean.setUpdateUserId(operatorId);
        goodNumberDao.insert(goodNumberBean);
        //绑定靓号到用户
        userDao.bindGoodNum(userId,goodNumber);
    }

    @Override
    public void untie(Long userId, Long operatorId) {
        UserBean userBean = userDao.selectById(userId);
        if (userBean == null){
            throw new BusinessException(ResultEnum.USER_NO_EXIST.getCodeStr(),ResultEnum.USER_NO_EXIST.getMsg());
        }
        GoodNumberBean goodNumberBean = goodNumberDao.selectOne(new LambdaQueryWrapper<GoodNumberBean>().eq(GoodNumberBean::getUserId,userId));
        if (goodNumberBean==null){
            throw new BusinessException(ResultEnum.PLAYER_NO_GOOD_NUM.getCodeStr(),ResultEnum.PLAYER_NO_GOOD_NUM.getMsg());
        }
        goodNumberDao.deleteById(goodNumberBean.getId());
        //取消靓号
        userDao.untieGoodNum(userId);
    }


}
