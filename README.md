# 📈 Client Relationship Management (CRM) system
![STSTUS](https://img.shields.io/github/license/agiklo/CRM-System)
## General info
System (REST API) that enables the management of suppliers, employees, customers and products. Only authorized users with the rank of at least employee have access to the system functionality. Managers can create reports on products, customers, suppliers, etc. Such a report can be exported to a PDF or Excel file. The system is designed in such a way that only an administrator can register an account for a new user. The administrator fills in the registration form with the data of the new employee, then the employee receives an e-mail in which he must confirm the activation of the account. The system has a forum where employees can present their ideas and communicate with each other.

Documentation available at:
```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v2/api-docs
```

## Front-end
Currently, I started building the frontend part using React. The repository can be found in the link below.
https://github.com/agiklo/crm-react-frontend

## Technologies
- Java 8
- Spring (Boot, Data, Security)
- JWT
- Hibernate
- Oracle 18c Express Edition
- PostgreSQL 13
- Lombok
- OpenPDF
- Apache POI
- MapStruct

```
⚠️⚠️⚠️
Remember to install the Lombok plugin in your IDE, more information in the link below:
github.com/mplushnikov/lombok-intellij-plugin#plugin-installation
```
```
docker run -p 8080:8080 --name agiklocrm -d agiklo/crm
```

## Features
- Possibility to export specific data to PDF and Excel files.
- Manage data about customers, suppliers, employees and products, etc.
- Possibility of registering a new account.
- Confirmation of account activation via email.
- Additional application security by JWT.
- Internal employee forum where employees can communicate with each other.

## 🖌️ Sample view in Angular
![Products](https://i.imgur.com/uVT6Xw8_d.webp?maxwidth=1520&fidelity=grand)

## 📄 Sample PDF export
![ProductsPDF](https://i.imgur.com/LxWxyJm.png)

## Database diagram<br>
![ERD](https://i.imgur.com/YUkRUnR.png)

## The future of the project
- Creating a frontend that enables data visualization.
