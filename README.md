# RulerView
尺子控件 RulerView
---
![FourStylesOfRuler](https://github.com/Ztech-G/RulerView/blob/master/screenshot/Four_styles_of_ruler.png)
# 属性：
|序号|  属性名   | 属性值  | 说明 |
|:--:|  :----:  | :----: | :--------: |
|1| lineColor  | color | 刻度线颜色|
|2| textColor  | color | 文本颜色|
|3| xOry| int | 刻度尺方向 (x轴/y轴)|
|4| pOrn| int | 刻度尺方向 (正向/反向)|
|5| textSize| dimension | 刻度值文本大小|
|6| lineWidth| dimension | 刻度线宽度|
|7| lineLength| dimension | 刻度线最大长度|
|8| lengthOfRuler| int | 尺子长度（单位：mm）|
# 使用：
```xml
<com.ztech.lib.RulerView
        android:id="@+id/rv1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#8A215CAC"
        app:lengthOfRuler="50"
        app:pOrn="POSITIVE"
        app:lineColor="#DE1414"
        app:textColor="#055223"
        app:textSize="20sp"
        app:lineLength="30dp"
        app:lineWidth="2dp"
        app:xOry="Y_AXIS" />
```