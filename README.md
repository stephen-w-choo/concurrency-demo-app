## Concurrency and thread-safety presentation

- A short lightning talk (15-20 mins) I gave about concurrency and thread safety.
- I have two demo apps with a simple counter that displays flaky behaviour due to improper handling of asynchronous behaviour.
- I've put the slides and the code for the demo apps (native iOS and native Android) here.
- Note - I am not a native iOS developer, and the code on the iOS side is absolutely cursed - it was easy enough to create thread-unsafe behaviour on Android, but I had to do some fairly cooked things to break thread safety in Swift. I don't quite understand the Swift concurrency model well enough.
