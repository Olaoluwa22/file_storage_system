# File Storage System
A simple file storage system application with RESTful endpoints for uploading and downloading files.

## Overview
The File Storage System provides endpoints to handle file uploads and downloads. It is designed to store and retrieve files efficiently and securely.

## Features
- Upload File: Upload files to the server.
- Download File: Download files from the server.
## Technology Stack
- Backend: Java with Spring Boot
- Database: PostgreSQL (for metadata storage)
- Storage: Local file system
- Build Tool: Maven
- Testing: JUnit, Mockito
## Endpoints
## Upload File
- URL: `` /api/v1/upload ``
- Method: POST
- Request Body: multipartfile
- Parameters:
file: The file to be uploaded.
- Response:
Status: 200 OK
- Body: String containing success message and file name.
#### Example Request:

```
POST http://localhost:8080/api/v1/upload
```

#### Example Response:

` File uploaded successfully: IMG_0579.PNG `

## Download File
 - URL: ``` /api/v1/download/{fileName} ```
- Method: GET
- Parameters:
fileName: The name of the file to be downloaded.
- Response:
Status: 200 OK
- Body: The file content.


#### Example Request:
 
```
GET "http://localhost:8080/api/v1/download/{fileName}
```

#### Example Response:

` An image of the requested resource. `

## Setup and Installation
### Prerequisites
- Java 11 or higher
- Maven
- PostgreSQL
 
## Clone the Repository

```
git clone https://github.com/olaoluwa22/file_storage_system.git
```

```
cd file_storage_system
```

## Configuration
Update application.properties with your database configuration:

### properties
- spring.datasource.url=jdbc:postgresql://localhost:5432/storageSystem
- spring.datasource.username=your_username
- spring.datasource.password=your_password
- spring.jpa.hibernate.ddl-auto=update
  
## Build and Run

```
mvn clean install
```

```
 mvn spring-boot:run 
```

## Database Setup
- Ensure PostgreSQL is running and create the database:
- createdb storageSystem
  
## Testing
- Unit tests are located in the src/test/java directory.
- Run the tests using:
```
mvn test
```

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License
```

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Contact
For any questions or feedback, please reach out to olaoluwaoni22@gmail.com.
