package com.coinsthai.controller.guest;

import com.coinsthai.controller.BaseController;
import com.coinsthai.converter.CoinApiViewConverter;
import com.coinsthai.vo.CoinApiView;
import com.coinsthai.vo.CoinView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 
 */
@Api(description = "币种API")
@RestController
@RequestMapping("/api/coins")
public class CoinController extends BaseController {

    @Autowired
    private CoinApiViewConverter apiViewConverter;

    @ApiOperation(value = "获取所有币种")
    @GetMapping()
    public List<CoinApiView> listAll() {
        List<CoinView> list = coinService.listActives();
        return apiViewConverter.toList(list);
    }

    @ApiOperation(value = "获取所有基准币种")
    @GetMapping("/bases")
    public List<CoinApiView> listBases() {
        List<CoinView> actives = coinService.listActives();
        List<CoinView> list = actives.stream().filter(view -> view.isBase()).collect(Collectors.toList());
        return apiViewConverter.toList(list);
    }

    @ApiOperation(value = "获取指定ID的币种")
    @GetMapping("/{id}")
    public CoinApiView get(@PathVariable String id) {
        id = translateNameToId(id);
        return apiViewConverter.toPojo(coinService.get(id));
    }
}
