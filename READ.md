### Overview
Spring security sample code using kotlin.

- Reference
  - [Spring解体新書 セキュリティ編　Spring Securityを徹底解説: 記憶喪失になっても忘れはいけないセキュリティ対策集](https://www.amazon.co.jp/Spring%E8%A7%A3%E4%BD%93%E6%96%B0%E6%9B%B8-%E3%82%BB%E3%82%AD%E3%83%A5%E3%83%AA%E3%83%86%E3%82%A3%E7%B7%A8-Spring-Security%E3%82%92%E5%BE%B9%E5%BA%95%E8%A7%A3%E8%AA%AC-%E8%A8%98%E6%86%B6%E5%96%AA%E5%A4%B1%E3%81%AB%E3%81%AA%E3%81%A3%E3%81%A6%E3%82%82%E5%BF%98%E3%82%8C%E3%81%AF%E3%81%84%E3%81%91%E3%81%AA%E3%81%84%E3%82%BB%E3%82%AD%E3%83%A5%E3%83%AA%E3%83%86%E3%82%A3%E5%AF%BE%E7%AD%96%E9%9B%86%E3%80%82-%E7%94%B0%E6%9D%91%E9%81%94%E4%B9%9F-ebook/dp/B08BFLJ47Z)

### How to run
```
// run mysql
docker-componse up -d

./gradlew bootRun 
```

### Users    
|  usename  |  password  | role | description
| ---- | ---- | ---- | ---- |
|  system  |  password  | ROLE_ADMIN, ROLE_GENERAL | admin user 
|  user  |  password  | ROLE_GENERAL | general user
|  sample1  |  password  | ROLE_GENERAL | password expired user
|  sample2 |  password  | ROLE_GENERAL | locked user
|  sample3 |  password  | ROLE_GENERAL | invalid user
|  sample4  |  password  | ROLE_GENERAL | account expired user