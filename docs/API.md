# Queue Web Control HTTP API

When OBS Scene Queue runs and the configuration property `httpApiServerEnabled` is enabled, OBS Scene Queue will start an HTTP server on the configured port on the local machine. This server listens for API calls as described below.

The root endpoint for the API is: `/api/v1`. The endpoints below are relative to this endpoint. These endpoints may return any of the described objects below.

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
| POST | `/<index:number>` | `QueueItem?` | Activates and returns the Queue item at the given queue index. Returns `null` if no item is found. <p>Query parameters: <br/>- `activateNextSubQueueItems=(true/false)`</p> |

#### Examples

Retrieve the Queue list:
```bash
curl -X GET http://localhost:8080/api/v1/queue/list
```

Activate the next Queue item:
```bash
curl -X POST http://localhost:8080/api/v1/queue/next
```

Activate the first Queue item:
```bash
curl -X POST http://localhost:8080/api/v1/queue/0
```

Activate the first Queue item without activating any sub items:
```bash
curl -X POST http://localhost:8080/api/v1/queue/0?activateNextSubQueueItems=false
```

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

Execute the first Quick Access Button:
```bash
curl -X POST http://localhost:8080/api/v1/quickAccessButtons/0
```
