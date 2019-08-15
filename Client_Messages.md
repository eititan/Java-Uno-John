### Client Actions to send to Server:

#### DRAW CARD (**proposed changes are done**)

```json
{
    "type": "application",
    "message":
        {
            "module":"JavaUno",
            "action": "draw card"
        }
}
```

#### PLAY CARD (_**done**_)

```json
{
    "type": "application",
    "message":
        {
            "module":"JavaUno",
            "action":
                {
                    "play card":
                        {
                            "card":
                                {
                                    "type": "3",
                                    "color": "Blue"
                                }
                        }
                }
        }
}
```

#### CALL UNO (_**done**_)

```json
{
    "type": "application",
    "message":
        {
            "module":"JavaUno",
            "action":
                {
                    "call uno": "Chris"
                }
        }
}
```

```json
{
    "type": "application",
    "message":
        {
            "module":"JavaUno",
            "action":
                {
                    "color change": "Red"
                }
        }
}
```

#### JOIN (_**done**_)

```json
{
    "type": "application",
    "message":
        {
            "module":"JavaUno",
            "action":"join"
        }
}
```