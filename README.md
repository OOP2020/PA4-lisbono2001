Programming 2 Final Project

# Typing game with score collector

By Theetouch Kasemarnontana

## About the application

This project is a graphical user interface for practicing your typing speed skill through the game. 

Input player name and waiting page for loading information from the sheets also for players to prepare themself.


![](https://github.com/OOP2020/PA4-lisbono2001/blob/master/Photoes/GUI6.jpg)


Game play and show “Game Over” when you missed 10 words.

![alt text](https://github.com/OOP2020/PA4-lisbono2001/blob/master/Photoes/GUI7.jpg)



When the game ends, the player information will be written to google sheets.

![alt text](https://github.com/OOP2020/PA4-lisbono2001/blob/master/Photoes/GUI5.png)

(You can check the score board through this link https://docs.google.com/spreadsheets/d/1-j8qRJTLG_u9n3AWTVQ5B-N2fLM3Kz1xL0wvH88Pfv4/edit#gid=0 )

Here is UML diagram

![](https://github.com/OOP2020/PA4-lisbono2001/blob/master/Photoes/UML.png)

## Major features

- Falling words will increase its speed over time.
- You can modify the program to get more words ( default is 300 different words ).
- Display your progression over time ( accuracy , missed word and score ).
- When you finish the game and you are connected to the internet the program will add your name, score ,accuracy and time to store in Google spreadsheets.
- If you connect to the internet and come back to play the game again, the program will find your name in the spreadsheets and it can tell you what's your highest score.

## How to run
for .jar file
'''
java --module-path {your JavaFX lib directory} --add-modules javafx.controls,javafx.fxml -jar PA4-lisbono2001.jar
'''

for .java file 
'''
open MenuPage.java and run
'''

## Technology used
- JavaFX 11.0.2.
- Google API library.

## Education Value
- Practiced and Learned more about JavaFX.
- Reviewed java I/O.
- How to connect a java program to Google spreadsheets and deal with Google client.
- How to use threads in an application.
- Improved my design pattern programming.

Note : After this I will improve the program, make the game more playful and more user friendly.
