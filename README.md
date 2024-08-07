# Profiler

I call it the Profiler because it scrapes any digital data about someone and performs all the background checks automatically to build a profile. Its aim is to produce results with 90+ percent accuracy, such as phone numbers, addresses and work status. It could be dangerous, so use it cautiously. However, I don't consider it unethical, as all the data it could ever find was exposed by the user to the internet in the first place; all it does is organize it.

## Progress...
So at the moment I can scrape all Google, Linkedin and Facebook data of a particular target and save them in the database. Should I repeat the search, instead of scraping from Google, it will load the results from the database so I can process it.

I haven't done any data processing yet, but NLP is the great choice, a little bit of Stanford CoreNLP and Spring.ai will do the trick.

In there, I can also automatically log into Facebook and LinkedIn and search our targets.
I've noticed that automating the login process on social media, raises some flags so I log in with cookies instead. But due to other priorities, I've stopped here. Wait for more updates. 


## Inspiration
I have long lost friends to find, also I got the inspiration from movies. Realizing fiction is my goal. So instead of waiting for someone else to give me their system, I'm making mine.

## Key Tools

I tried using public APIs but they were restricting so I resorted in automation libraries. What I've used below:

- Selenium for automation
- Jsoup
- Maven
- Java
- Springboot
- JPA

## Demo

#### Google Search

- You can search your person automatically.
  
![google_search](https://github.com/anakiebn/Profiler/blob/main/Screenshot%20(37).png)

#### Json Data

- You can do whatever you want with this data, it's usable in JSON.
  
![json_data](https://github.com/anakiebn/Profiler/blob/main/Screenshot%20(38).png)

