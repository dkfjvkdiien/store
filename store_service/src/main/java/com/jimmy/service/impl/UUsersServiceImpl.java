package com.jimmy.service.impl;

import com.jimmy.dao.IUUsersDao;
import com.jimmy.domain.UUsers;
import com.jimmy.service.IUUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userService")
@Transactional
public class UUsersServiceImpl implements IUUsersService {

    @Autowired
    IUUsersDao usersDao;

    @Override
    public Integer saveOrUpdate(UUsers users) throws Exception {
        if(users.getId() == null){
            users.setDate(new Date());
            return usersDao.save(users);
        }else {
            return usersDao.update(users);
        }
    }

    @Override
    public Integer detractMoney(Integer total, Integer id) throws Exception {
        UUsers byId = usersDao.findById(id);
        Double money = byId.getMoney();
        if(money < total) {
            return -1;
        } else {
            money -= total;
            return usersDao.detractMoney(money, id);
        }
    }

    @Override
    public Integer update(UUsers users) throws Exception {
        return usersDao.update(users);
    }

    @Override
    public Integer register(UUsers users) throws Exception {
        return usersDao.save(users);
    }

    @Override
    public UUsers findById(Integer id) throws Exception {
        return usersDao.findById(id);
    }

    @Override
    public UUsers findByEmail(String email) throws Exception {
        return usersDao.findByEmail(email);
    }

    @Override
    public UUsers findByTelephone(String telephone) throws Exception {
        return usersDao.findByTelephone(telephone);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UUsers users;
        User user = null;
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        System.out.println(s);
        try {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            System.out.println(authorities.get(0));
            if(s.contains("@")){
                users = usersDao.findByEmail(s);
                user = new User(users.getEmail(), users.getPassword(), users.getIs_ban() == 2, true, true,true, authorities);
            }else{
                users = usersDao.findByTelephone(s);
                user = new User(users.getTelephone(), users.getPassword(),users.getIs_ban() == 2, true,true, true, authorities);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

/*    private List<SimpleGrantedAuthority> getAuthority(String role){
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        String[] roles = role.split(",");

        for (int i = 0; i <= roles.length; i++){
            list.add(new SimpleGrantedAuthority("ROLE_"+roles[i]));
        }

        return list;
    }*/
}

