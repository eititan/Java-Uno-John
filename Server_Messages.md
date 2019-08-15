### Server Notifications to send to Client:

#### DRAW CARD (_**done**_)

```json
{
    "draw card": 
        {
            "card":
                {
                    "type": "5",
                    "color": "Red"
                }
        }
}
```

#### DREW CARD (_**done**_)

```json
{
    "notify":
        {
            "drew card": "Chris"
        }
}
```
#### PLAYED CARD (_**done**_)

```json
{
    "notify":
        {
            "played card": 
                {
                    "username" : "Chris",
                    "type": "6",
                    "color": "Red"
                }
        }
}
```

#### CALLED UNO (_**done**_)

```json
{
    "notify":
        {
            "called uno": "Chris"
        }
}
```

```json
{
    "notify":
        {
            "game will started": "Game will start"
        }
}
```

#### STARTED (_**done**_)

```json
{
    "notify":
        {
            "game started": "Game has started"
        }
}
```

```json
{
    "notify":
        {
            "game ended": "Game has ended"
        }
}
```

#### JOINED (_**done**_)

```json
{
    "notify":
        {
            "client joined": "Chris"
        }
}
```

```json
{
    "notify":
        {
            "color changed": "Blue"
        }
}
```

```json
{
    "notify":
        {
            "player skipped": "John"
        }
}
```

```json
{
    "notify":
        {
            "reversed": "counter-clockwise"
        }
}
```

#### TURN CHANGED (_**done**_)

```json
{
    "notify":
        {
            "turn changed": "Chris"
        }
}
```

```json
{
    "notify":
        {
            "winner": "Chris"
        }
}
```

```json
{
    "notify":
        {
            "reshuffle" : "Dealer reshuffling"
        }
}
```

#### YOUR TURN (_**done**_)

```json
{
    "notify":
        {
            "your turn" : "Brenden"
        }
}
```

```json
{
    "notify": 
        {
            "top card": { "color":  "<color>", "type":  "<value>" }
        }
}
```

```json
{
    "notify": 
        {
            "card count": 
                {
                    "<username>": "<count>",
                    "<username 2>": "<count 2>"
                }
        }
}
```