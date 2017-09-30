# EasyAddressPicker
中国省市县地址选择器

### 效果图
![](https://github.com/wangnaiwen/EasyAddressPicker/blob/master/image/%E7%82%B9%E5%87%BB%E5%BC%B9%E6%A1%86.png) 
![](https://github.com/wangnaiwen/EasyAddressPicker/blob/master/image/%E6%95%88%E6%9E%9C%E5%9B%BE.png)

### 功能描述

这是一个基于Android的地址选择器

### 使用方法
Step 1：添加maven仓库
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2：添加依赖
```
dependencies {
	        compile 'com.github.wangnaiwen:EasyAddressPicker:v1.0.1'
}
```
Step 3：在Activity中调用
```
new PickLocateDialog(MainActivity.this, new AddressPickedListener() {
    @Override
    public void onAddressPicked(ProvinceModel province, CityModel city, DistrictModel district) {
        //注意：有些地区没有区/县,所以要判断district是否为null
    }
});
```

联系方式:  
QQ：1464521469
