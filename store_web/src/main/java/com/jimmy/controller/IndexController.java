package com.jimmy.controller;

import com.jimmy.domain.UGoods;
import com.jimmy.domain.UGoodsType;
import com.jimmy.service.IUGoodsService;
import com.jimmy.service.IUGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    IUGoodsService goodsService;
    @Autowired
    IUGoodsTypeService goodsTypeService;

    @RequestMapping("")
    public ModelAndView index() throws Exception{
        ModelAndView mv = new ModelAndView();

        List<UGoodsType> goodsTypes = goodsTypeService.getAll();
        List<UGoods> hotGoods = goodsService.getHotGoods();
        List<UGoods> recommendedGoods = goodsService.getRecommendedGoods();
        recommendedGoods = recommendedGoods.parallelStream().peek(x -> {

            try {
                UGoodsType byId = goodsTypeService.getById(x.getTypeid());
                x.setTypeStr(byId.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).collect(Collectors.toList());
        hotGoods.add(hotGoods.get(0));

        mv.addObject("goodstype", goodsTypes);
        mv.addObject("hgoods", hotGoods);
        mv.addObject("rgoods", recommendedGoods);
        mv.setViewName("index");

        return mv;
    }


}
