# FITTING AVATAR_Favatar
AI 기반 가상 피팅 & 3D Avatar 기술 구현
- Team: subin CHO, 전재억, 천재혁, 최강희
## Page_Diagram
![software drawio](https://github.com/user-attachments/assets/c692aa7b-29d5-4129-b91a-1ffb647ee56b)

Project Taget!
- Virtual fittings tailored to your body shape allow you to intuitively understand the overall feeling when you actually wear them
- Customized visual feedback allows you to check the fit of clothes that fit the individual consumer's body instead of comments or reviews, providing visual information

## 📜 Workflow
![software drawio](https://github.com/user-attachments/assets/c692aa7b-29d5-4129-b91a-1ffb647ee56b)

## 💡 IDEA
# Inpainting
Repaint-Seg model => Two components
fseg -> Image segmentation method
finp -> Image inpainting method

- Perform segmentation on the input image to output the segmented image m
- Afterwards, use m to perform Inpainting

# 3D Avatar
- Head 영역 중심의 모델 기반 3D Avatar 텍스쳐 개선 예정

### Demo

  
<p align="center">
  <a href="https://star-history.com/#geekyutao/Inpaint-Anything&Date">
    <img src="https://api.star-history.com/svg?repos=geekyutao/Inpaint-Anything&type=Date" alt="Star History Chart">
  </a>
</p>
