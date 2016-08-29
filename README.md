# HeaderWaveView

仿百度外卖个人页，头像随波浪漂移效果

###Screenshot：

![img](https://github.com/sobinyuan/HeaderWaveView/blob/master/demo.gif)

###How to Use：

- 1、添加HeaderWaveView到布局文件

- 2、实例化HeaderWaveHelper 

```
//behindWaveColor 波浪背后颜色
//frontWaveColor 波浪前面颜色
//view 随波浪浮动的view
new HeaderWaveHelper(HeaderWaveView HeaderWaveView, int behindWaveColor, int frontWaveColor, View view)
```

- 3、start && cancel

```
mHeaderWaveHelper.start();
mHeaderWaveHelper.cancel();
```
 
- 4、Setter methods

```
//水位高低变化 waterLevelRatio 
setDefaultWaterLevelRatio(float defaultWaterLevelRatioF,float defaultWaterLevelRatioT)
//波浪大小振幅 amplitudeRatio 
setDefaultAmplitudeRatio(float defaultAmplitudeRatioF,float defaultAmplitudeRatioT) 
//floatView 旋转调整角
setDefaultFloatViewRotation(float defaultFloatViewRotation)
```

### Reference
参考[WaveView](https://github.com/gelitenight/WaveView)实现
