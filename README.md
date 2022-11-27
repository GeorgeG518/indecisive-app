# indecisive-app
# Requirements
1. Android Studio, version greater than > Chipmunk (2021.2.1)

# Build and Install Directions
If you are on windows, download git for windows: https://gitforwindows.org/ If you are on Mac, you can skip this part. 

If you haven’t already, download Android Studio. 

Open up some sort of terminal. If you are on windows, press windows key and then type command prompt. If on Mac, use the built in terminal.  

In the terminal, navigate to where you want to have the folder of all of the indecisive files. This can be AndroidStudioProjects [where ever that is] or any folder of your choosing. 

Copy this command and hit enter: git clone https://github.com/GeorgeG518/indecisive-app.git 

This will clone the repository on your local machine into a folder at the location your terminal is at I.e. if I was in Documents, I should see a new folder: Documents/indecisive-app 

Before you go further: create/steal from another project a file called local.properties and place something the following in it. 
NOTE: sdk.dir will be unique. Replace Owner with your username if you’re on windows. 
I’m afraid idk where it is on Mac, but you can make an android studio project and then look at where its local properties is: 
sdk.dir=C\:\\Users\\Owner\\AppData\\Local\\Android\\Sdk 

Once you have your local.properties file created, put it in the indecisive-app/indecisive folder. You may need another in the app folder below that. 

Open up AndroidStudio. If it opens up a project, save your work, click File in the top left and then close project. You will get to a screen like this. Click Open.:  

Once you click open, you will want to find where you did that git clone.
In my case, I cloned it at Users\Owner\indecisive-app\ . You will want to expand that folder and then open the android studio project called indecisive. 
Don’t click app. 


It will attempt to open up a project. If it says anything about updating AndroidStudio or gradle, do that. It may also have to sync project files, so if its hangs up, give it a second. 

For the emulator, I suggest using Pixel 2 with API30 because it gives me the least trouble 

