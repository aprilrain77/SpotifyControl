# SpotifyControl
 Control your Spotify from Minecraft! :)

How is it done? 

Windows: It's primarily done via using JNA, for example to get the current playing song, it enumerates the process list to find the "Spotify.exe" and then it enumerates it's windows to find the "Chrome_WidgetWin_1" class, which shows the current playing song

Linux: playerctl makes things easy :)

Commands:
- /spotify hud color <background|text> (rgb/rgba)
- /spotify hud margin <pixels>
- /spotify hud toggle
- /spotify hud preset <bottomcenter|bottomleft|bottomright|middlecenter|middleleft|middleright|topcenter|topleft|topright> 
- /spotify hud position <x> <y>

https://modrinth.com/mod/spotify-control

https://discord.gg/k7XJBPfWeD discord for it :3
