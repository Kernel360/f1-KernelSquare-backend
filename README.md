# f1-KernelSquare
지속가능한 성장을 위한 개발자 커뮤니티 [KernelSquare](https://kernelsquare.live)

## 프로젝트 목적 및 기획
- 소통을 통해 꾸준한 성장을 도모하는 개발자들을 위한 공간을 제공하기 위함
- Stack Overflow를 벤치마크한 Q & A 서비스
- 예약 시스템 기반 멘토와 멘티 1:1 커피챗 서비스
- 정해진 장소와 시간에 모여서 각자 코딩하는 모각코 서비스

## 프로젝트 소개
| 렌딩 페이지 | 회원가입 |
| --- | --- |
| ![렌딩페이지_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/2c1f91e7-e9a3-4449-94ef-10467a52d06e) | ![회원가입_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/cdce8833-d566-4f7d-84f1-b02a2f8fe641) |
| 커널스퀘어의 서비스에 관심을 가질 수 있도록 합니다. | 정보를 조건에 맞게 입력하고 이메일과 닉네임 중복확인 통과되면 회원가입을 할 수 있습니다. |

| 로그인 | 마이페이지 |
| --- | --- |
| ![로그인_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/49d75024-d7b7-4be1-a9b7-9ea541d8a886) | ![마이페이지_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/bb93e5aa-aafa-4866-ae06-2fc6fad5b8f7) |
| 이메일과 비밀번호를 입력하는 일반 로그인과 깃허브 소셜 로그인을 지원합니다. | 회원 정보를 볼 수 있는 마이페이지입니다. 본인이라면 프로필 사진, 닉네임, 자기소개를 변경할 수 있습니다. |

| 질문 | 답변 |
| --- | --- |
| ![질문생성_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/2250e06d-f807-408d-ad82-11808da768bc) | ![답변생성_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/312a3e79-194e-453b-a38f-07c19dd5b90a) |
| 제목, 기술 스택, 내용을 입력하여 질문을 생성할 수 있습니다. 질문을 등록하면 잠시 후 AI가 답변을 해줍니다. | 질문에 답변할 수 있습니다. 다른 사람의 답변에 투표를 할 수 있습니다. |

| 알림 | 커피챗 |
| --- | --- |
| ![알림_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/02fbe714-0fbd-4cf6-b6fa-fa7d65e6d842) | ![커피챗_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/6b7b28c0-1f33-4450-8865-91e3b627d387) |
| 자신의 질문에 답변이 달렸을 때, 자신의 답변이 랭킹에 들었을 때, 커피챗 요청이 들어왔을 때 알림이 옵니다. | 멘토 자격을 가진 사용자가 커피챗을 생성할 수 있고 다른 사용자는 생성된 커피챗을 열린 시간에 예약할 수 있습니다. |

| 채팅 | 모각코 |
| --- | --- |
| ![채팅_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/c7c13359-b8d4-4a11-800b-105de5403fa8) | ![모각코_gif](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/56c529b2-1b8b-42f6-a4f5-6d396d66ffa6) |
| 예약된 시간이 되면 예약한 사람은 1대1 채팅방에 입장할 수 있습니다. | 제목, 장소, 인원 수, 시간, 내용을 적으면 모각코를 생성할 수 있습니다. 참가할 사람은 댓글로 남길 수 있습니다. |

## 기술스택
<div>
  <img src="https://img.shields.io/badge/openjdk-437291?style=flat&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=flat&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/redis-DC382D?style=flat&logo=redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/flyway-CC0200?style=flat&logo=flyway&logoColor=white"/>
  <img src="https://img.shields.io/badge/amazons3-569A31?style=flat&logo=amazons3&logoColor=white"/>
  <img src="https://img.shields.io/badge/mongodb-47A248?style=flat&logo=mongodb&logoColor=white"/>
  <img src="https://img.shields.io/badge/apachekafka-231F20?style=flat&logo=apachekafka&logoColor=white"/>
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=flat&logo=amazonec2&logoColor=white" />
  <img src="https://img.shields.io/badge/docker-2496ED?style=flat&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/githubactions-2088FF?style=flat&logo=githubactions&logoColor=white"/>
  <img src="https://img.shields.io/badge/prometheus-E6522C?style=flat&logo=prometheus&logoColor=white"/>
  <img src="https://img.shields.io/badge/grafana-F46800?style=flat&logo=grafana&logoColor=white"/>
</div>


## 아키텍처
- 멀티모듈

![멀티모듈_all](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/2536ac8b-80d3-47c5-92f4-5e550a25a4cd)



- 전체 인프라

![전체인프라2](https://github.com/Kernel360/f1-KernelSquare-backend/assets/97713997/25de0c61-b20f-41fe-8a1b-12d1a84c728c)

## 팀원
<table>
  <tr>
    <td align="center" width="120px">
<!--       <a href="">  
        <img src="" alt="" />
      </a> -->
      <h4>김원상</h2>
    </td>
   <td align="center" width="120px">
      <h4>문찬욱</h2>
    </td>
    <td align="center" width="120px">
      <h4>홍주광</h2>
    </td>
    <td align="center" width="120px">
      <h4>고병룡</h2>
    </td>
  </tr>
</table>

## admin 레포지토리(어드민, 알림, 채팅, 모니터링)
[KernelSquare-Admin](https://github.com/Kernel360/f1-KernelSquare-admin-backend)
