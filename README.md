# AndroidStudentGroupApp
Application for groups of students and their teachers

This application will help teachers to organize varios information connected with learning process like news, lessons' detailed info, hometasks, schedule, table and etc.

Student will be able to access necessary information in fast and convenient way. 

Also this application provide chat for every group and there is a streaming functionality for distance lecturing.

# Testing teachers' functionality
Test accounts(login:pass):
1. rollingworld:1 (Two groups)
2. nagibator228:1 (One group)

To test teachers' features change the "isStudent" field inside UserEntity class

# Testing streaming
On the first device head to Profile-Streaming, adjust parameters and as endpoint use "rtmp://server-ip:1935/live/your-stream-key". "server-ip" depends on your testing environment, as "your-stream-key" you can use group name or other char sequence.

On the second device head to Profile-WatchStream, enter the same endpoint and press "Start player" button.
Btw, this player can be used to view other HLS content.
