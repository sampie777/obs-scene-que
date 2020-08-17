# Queue Web Control HTTP API

When OBS Scene Queue runs and the configuration property `httpApiServerEnabled` is enabled, OBS Scene Queue will start an HTTP server on the configured port on the local machine. This server listens for API calls as described below.

The root endpoint for the API is: `/api/v1`. The endpoints below are relative to this endpoint. These endpoints may return any of the described objects below.

Responses are following the [JSON API](https://jsonapi.org/) guidelines, but due to limited implementation, errors will not be shown. Instead, a `"data": null` response will be given. The error messages can be retrieved from the *Notifications* endpoint. 

## Objects

### Queue

| Property | Type | Description |
| --- | --- | --- |
| `name` | string | Name of the current Queue (file) |
| `applicationVersion` | string | Version of OBS Scene Queue used to create this Queue |
| `queueItems` | array<QueueItem> | Ordered list of Queue items in this Queue |
| `apiVersion` | number | Version of the Queue file API (not the HTTP API) | 

JSON: 
```json
{
  "name": "string",
  "applicationVersion": "string",
  "queueItems": [],
  "apiVersion": number
}
```

### QueueItem

| Property | Type | Description |
| --- | --- | --- |
| `pluginName` | string | Name of the plugin which this item belongs to |
| `className` | string | Classname of this item |
| `name` | string | Display name in the Queue list |
| `executeAfterPrevious` | boolean | Execute/activate this item with activation of previous Queue item |
| `quickAccessColor` | object | Java color object for the background color of this item in the Queue list |
| `data` | object | Extra data fields which differ per plugin |

JSON: 
```json
{
  "pluginName": "string",
  "className": "string",
  "name": "string",
  "executeAfterPrevious": boolean,
  "quickAccessColor": {
    "value": number,
    "falpha": number
  },
  "data": {}
}
```

### Notification

| Property | Type | Description |
| --- | --- | --- |
| `subject` | string | Notification subject |
| `message` | string | Notification content |
| `timestamp` | string | Timestamp when the Notification was created |

JSON: 
```json
{
  "timestamp": "2020-07-26T13:38:27.046+0200",
  "message": "string",
  "subject": "string"
}
```

### ConfigPair

| Property | Type | Description |
| --- | --- | --- |
| `key` | string | Name of the config property |
| `value` | string | Value of the config property |

JSON: 
```json
{
  "key": "string",
  "value": object
}
```

### Plugin

| Property | Type | Description |
| --- | --- | --- |
| `name` | string | Internal plugin name |
| `description` | string | Description of the plugin |
| `version` | string | Version of the plugin |

JSON: 
```json
{
  "name": "string",
  "description": "string",
  "version": "string"
}
```

## Endpoints

### Queue

Relative API endpoint: `/queue`

| Method | Relative endpoint | Response | Description |
| --- | --- | --- | --- |
| GET | `/list` | `Queue` | Returns a `Queue` object with information about the current Queue, including a list of all Queue items. |
| GET | `/current` | `QueueItem?` | Returns the current active Queue item. Returns `null` if no item is active. |
| POST | `/current` | `QueueItem?` | Reactivates and returns the current active Queue item. Returns `null` if no item is active. |
| GET | `/previous` | `QueueItem?` | Returns the previous Queue item. Returns `null` if no previous item is found. |
| POST | `/previous` | `QueueItem?` | Activates and the previous Queue item. Returns the (new) active Queue item. Returns `null` if no item is active. |
| GET | `/next` | `QueueItem?` | Returns the next Queue item. Returns `null` if no next item is found. |
| POST | `/next` | `QueueItem?` | Activates and the next Queue item. Returns the (new) active Queue item. Returns `null` if no item is active. |
| GET | `/<index:number>` | `QueueItem?` | Returns the Queue item at the given queue index. Returns `null` if no item is found. |
| POST | `/<index:number>` | `QueueItem?` | Activates and returns the Queue item at the given queue index. Returns `null` if no item is found. <p>Query parameters: <br/>- `activateNextSubQueueItems=<true/false>`</p> |
| DELETE | `/<index:number>` | `QueueItem?` | Removes and returns the removed Queue item at the given queue index. Returns `null` if no item is found. |
| POST | `/add` | `QueueItem?` | Adds the given Queue item at the end of the Queue. Returns `null` if this failed. |
| POST | `/add/<index:number>` | `QueueItem?` | Adds the given Queue item at the given index in the Queue. Returns `null` if this failed. |

#### Examples

Retrieve the Queue list:
```bash
curl -X GET http://localhost:8080/api/v1/queue/list
```
> Response:
> ```json
> {
>   "data": {
>     "name": "default-que",
>     "applicationVersion": "2.6.0",
>     "queueItems": [
>       {
>         "pluginName": "MockPlugin",
>         "className": "QueItemMock",
>         "name": "1",
>         "executeAfterPrevious": false,
>         "quickAccessColor": null,
>         "data": {}
>       },
>       {
>         "pluginName": "MockPlugin",
>         "className": "QueItemMock",
>         "name": "2",
>         "executeAfterPrevious": false,
>         "quickAccessColor": null,
>         "data": {}
>       },
>       {
>         "pluginName": "MockPlugin",
>         "className": "QueItemMock",
>         "name": "3",
>         "executeAfterPrevious": false,
>         "quickAccessColor": null,
>         "data": {}
>       }
>     ],
>     "apiVersion": 1
>   }
> }
> ```

Activate the next Queue item:
```bash
curl -X POST http://localhost:8080/api/v1/queue/next
```
> Response:
> ```json
> {
>   "data": {
>     "pluginName": "MockPlugin",
>     "className": "QueItemMock",
>     "name": "3",
>     "executeAfterPrevious": false,
>     "quickAccessColor": null,
>     "data": {}
>   }
> }
> ```

Activate the first Queue item:
```bash
curl -X POST http://localhost:8080/api/v1/queue/0
```

Activate the first Queue item without activating any sub items:
```bash
curl -X POST http://localhost:8080/api/v1/queue/0?activateNextSubQueueItems=false
```

Adds a new Queue item:
```bash
curl -X POST http://localhost:8080/api/v1/queue/add -d "{\"pluginName\": \"MockPlugin\",\"className\": \"QueItemMock\",\"name\": \"I'm new :)\",\"executeAfterPrevious\": false,\"data\": {}}"
```
> Response:
> ```json
> {
>   "data": {
>     "pluginName": "MockPlugin",
>     "className": "QueItemMock",
>     "name": "I'm new :)",
>     "executeAfterPrevious": false,
>     "quickAccessColor": null,
>     "data": {}
>   }
> }
> ```

### Quick Access Buttons

Relative API endpoint: `/quickAccessButtons`

| Method | Relative endpoint | Response | Description |
| --- | --- | --- | --- |
| GET | `/list` | `array<QueueItem>` | Returns a `Queue` object with information about the current Queue, including a list of all Queue items. |
| GET | `/<index:number>` | `QueueItem?` | Returns the Queue item of the Quick Access Button at the given index (note this index begins at 0, while the index in the GUI begins at 1). Returns `null` if the Quick Access Buttons is empty. |
| POST | `/<index:number>` | `QueueItem?` | Activates the Quick Access Button at the given index (note this index begins at 0, while the index in the GUI begins at 1). It returns the Queue item of this button. Returns `null` if the Quick Access Buttons is empty. |

#### Examples

Retrieve a list of all Quick Access Buttons:
```bash
curl -X GET http://localhost:8080/api/v1/quickAccessButtons/list
```
> Response:
> ```json
> {
>   "data": [
>     {
>       "pluginName": "MockPlugin",
>       "className": "QueItemMock",
>       "name": "1",
>       "executeAfterPrevious": false,
>       "quickAccessColor": null,
>       "data": {}
>     },
>     null,
>     {
>       "pluginName": "MockPlugin",
>       "className": "QueItemMock",
>       "name": "2",
>       "executeAfterPrevious": false,
>       "quickAccessColor": null,
>       "data": {}
>     }
>   ]
> }
> ```

Execute the first Quick Access Button:
```bash
curl -X POST http://localhost:8080/api/v1/quickAccessButtons/0
```
> Response:
> ```json
> {
>   "data": {
>     "pluginName": "MockPlugin",
>     "className": "QueItemMock",
>     "name": "2",
>     "executeAfterPrevious": false,
>     "quickAccessColor": null,
>     "data": {}
>   }
> }
> ```

### Notifications

Relative API endpoint: `/notifications`

| Method | Relative endpoint | Response | Description |
| --- | --- | --- | --- |
| GET | `/list` | `array<Notification>` | Returns a list of all Notifications. <p>Query parameters: <br/>- `unread=<true/false>`</p> |
| GET | `/last` | `Notification?` | Returns the most recently added Notification. Returns `null` if there are no Notifications. |
| POST | `/markAllAsRead` |  | Marks all Notifications as read. |
| POST | `/add` | `Notification` | Adds a new Notification. <p>Query parameters: <br/>- `markAsRead=<true/false>`<br/>- `popup=<true/false>`</p> |

#### Examples

Returns the last Notification:
```bash
curl -X GET http://localhost:8080/api/v1/notifications/last
```
> Response:
> ```json
> {
>   "data": {
>     "timestamp": "2020-07-26T13:38:27.046+0200",
>     "message": "Some thing went wrong during the test",
>     "subject": "Test Error"
>   }
> }
> ```

Add a new Notification and display it in a popup window in the GUI:
```bash
curl -X POST http://localhost:8080/api/v1/notifications/add?popup=true -d "{\"timestamp\": \"2020-07-26T13:38:27.046+0200\",\"message\": \"Some thing went wrong during the test\",\"subject\": \"Test Error\"}"
```
> Response:
> ```json
> {
>   "data": {
>     "timestamp": "2020-07-26T13:38:27.046+0200",
>     "message": "Some thing went wrong during the test",
>     "subject": "Test Error"
>   }
> }
> ```

### Config

Relative API endpoint: `/config`

| Method | Relative endpoint | Response | Description |
| --- | --- | --- | --- |
| GET | `/list` | `array<ConfigPair>` | Returns a list of ConfigPairs with the current configuration key/value pairs. |
| GET | `/<key:string>` | `ConfigPair` | Returns the configuration value of the given property key. Returns no value property or `"value": null` if the key doesn't exists. |

#### Examples

Returns the `obsReconnectionTimeout` property value:
```bash
curl -X GET http://localhost:8080/api/v1/config/obsReconnectionTimeout
```
> Response:
> ```json
> {
>   "data": {
>     "key": "obsReconnectionTimeout",
>     "value": 3000
>   }
> }
> ```

### Plugins

Relative API endpoint: `/plugins`

| Method | Relative endpoint | Response | Description |
| --- | --- | --- | --- |
| GET | `/list` | `array<Plugin>` | Returns a list of enabled plugins. |

#### Examples

Returns a list of the enabled plugins:
```bash
curl -X GET http://localhost:8080/api/v1/plugins/list
```
> Response:
> ```json
> {
>   "data": [
>     {
>       "name": "ObsPlugin",
>       "description": "Queue items for integration with OBS",
>       "version": "0.0.0"
>     },
>     {
>       "name": "Utility",
>       "description": "Queue items for just displaying text or adding delay",
>       "version": "0.0.0"
>     }
>   ]
> }
> ```
