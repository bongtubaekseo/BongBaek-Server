# 💌 봉투백서

<img width="4935" height="1758" alt="img_bongbaek_banner" src="https://github.com/user-attachments/assets/819d999b-3b57-4adc-b91a-44c104cf0a98" />


### **“경조사비, 얼마 내야 할지 매번 고민되시나요?”**
**봉투백서**는 **사회초년생을 위한 맞춤형 경조사비를 추천**하고, 효율적으로 관리할 수 있도록 도와주는 앱 서비스입니다.<br/>
매번 헷갈리던 경조사 금액을 알려드립니다.<br/>

더 이상 포털사이트에서 ‘결혼식 축의금 얼마’를 검색하지 마세요!<br/>

<br/>

## 🌟주요 기능

### 1️⃣ 온보딩 – 나를 위한 경조사비 기준 설정<br/>
유저의 상황을 반영한 맞춤형 추천을 위해<br/>
간단한 온보딩 과정에서 수입 등 기본 정보를 입력받아요.<br/>

이 정보를 바탕으로 더 정교한 경조사비 추천이 이루어집니다.<br/>

### 2️⃣ 홈 – 추천 금액과 행사 정보를 한눈에<br/>
홈 화면에서는 개인별 맞춤 금액과 경조사 세부 정보,<br/>
그리고 다가오는 일정을 한눈에 확인할 수 있어요.<br/>

이제 어떤 행사에 얼마를 내야 할지 더 이상 고민하지 않아도 돼요.<br/>

### 3️⃣ 금액 추천 - 상황에 맞는 금액, 정확하게<br/>
사용자의 입력 정보, 연락 빈도, 만남 정도를 바탕으로<br/>
가장 적절한 경조사비를 추천해줘요.<br/>

최소~최대 범위 제시로 유연하게 선택할 수 있고,<br/>
합리적인 금액 범위를 안내해요.<br/>

### 4️⃣ 기록하기 – 지난 경조사 내역도 꼼꼼하게<br/>
지금까지 참석했던 경조사 내역을 체계적으로 정리하여<br/>
언제, 누구에게, 얼마를 냈는지 관리가 쉬워집니다.<br/>

다음 경조사 때 참고하거나 지출을 관리할 때 유용해요.<br/>

<br/>

## **✨ Contributors**


|                              박경민 (Lead) <br> [@Kyoung-M1N](https://github.com/Kyoung-M1N)                   |                           김민경 <br> [@kyoooooong](https://github.com/kyoooooong)                             |                  조운재 <br> [@chounjae](https://github.com/chounjae)                             |
|:--------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------:|
| <img width="250" src="https://github.com/user-attachments/assets/4fffc68b-2334-4031-bb92-699d636d6b37"/> | <img width="250" src="https://github.com/user-attachments/assets/c32e516d-a181-49ad-a644-beac01223234"/> | <img width="250" src="https://github.com/user-attachments/assets/28690625-270a-485a-8353-a0b07596817b"/> |
<!--|                                              `금액추천`                                                    |                                              `온보딩`<br>`홈`                                               |                           `기록하기`<br>`상세내용`<br>                                                      |
-->
<br/>

## **⚒️ Tech Stacks**

### Cooperation

<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white"/> <img src="https://img.shields.io/badge/Github-222222?style=flat&logo=Github&logoColor=white"/> <img src="https://img.shields.io/badge/CodeRabbit-FF570A?style=flat&logo=CodeRabbit&logoColor=white"/>

<img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=notion&logoColor=white"/> <img src="https://img.shields.io/badge/Discord-5865F2?style=flat&logo=discord&logoColor=white"/>

### Application

<img src="https://img.shields.io/badge/Java-21-007396?style=flat&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/Spring%20boot-3.5.3-6DB33F?style=flat&logo=Springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring%20Security-3.5.3-6DB33F?style=flat&logo=Springsecurity&logoColor=white"/>

<img src="https://img.shields.io/badge/JWT-0.12.6-000000?style=flat&logo=jsonwebtokens&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-2.8.9-85EA2D?style=flat&logo=Swagger&logoColor=white"/>

### Data 

<img src="https://img.shields.io/badge/Spring%20Data%20JPA-3.5.3-6DB33F?style=flat&logo=hibernate&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-8.0.41-4479A1?style=flat&logo=MySQL&logoColor=white"/>  <img src="https://img.shields.io/badge/QueryDSL-5.1.0-0094FF?style=flat&logo=OSF&logoColor=white"/>

### Infrastructure

<img src="https://img.shields.io/badge/Github Actions-2088FF?style=flat&logo=githubactions&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white"/>

<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat&logo=amazonec2&logoColor=white"/> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat&logo=amazonrds&logoColor=white"/>

<br/>

## **🗂️ Project Structure**

```
🗃️ bongbaek
├─ 🗃️ domain
│  ├─ 📁 common
│  ├─ 🗃️ event
│  │  ├─ 📁 code
│  │  ├─ 📁 controller
│  │  ├─ 📁 dto
│  │  │  ├─ 📁 common
│  │  │  ├─ 📁 request
│  │  │  └─ 📁 response
│  │  ├─ 📁 entity
│  │  ├─ 📁 exception 
│  │  ├─ 📁 repository
│  │  └─ 📁 service
│  └─ 🗃️ member
│     ├─ 📁 code
│     ├─ 📁 controller
│     ├─ 📁 dto
│     │  ├─ 📁 common
│     │  ├─ 📁 request
│     │  └─ 📁 response
│     ├─ 📁 entity
│     ├─ 📁 exception 
│     ├─ 📁 repository
│     └─ 📁 service
│
├─ 🗃️ global
│  ├─ 🗃️ api
│  ├─ 🗃️ common
│  ├─ 📁 config
│  ├─ 📁 exception
│  ├─ 🗃️ jwt
│  │  ├─ 📁 dto
│  │  ├─ 📁 enums
│  │  ├─ 📁 exception
│  │  ├─ 📁 util
│  └─ 🗃️ oauth
│     └─ 📁 kakao
│
└─ ▶️ BongbaekApplication
```

<br/>

## **🏗️ Service Architecture**

<img width="800" height="885" alt="서비스 아키텍쳐" src="https://github.com/user-attachments/assets/40d60d95-6357-4e73-bdb6-b8b453152315" />


<br/>
