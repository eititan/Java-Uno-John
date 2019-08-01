### Server Notifications to send to Client:

```json
{
    "notify":
        {
            "drew card": "Chris"
        }
}
```

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
