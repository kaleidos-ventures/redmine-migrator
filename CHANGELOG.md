## 1.0.1

### Issues

- Filtering public projects that not belong to the user
- Workaround to avoid user migration membership creation in Taiga
- Add Redmine default timeout. 10 seconds
- Fix bug related to GUI when updating redmin project list
- Fix bug related to checking service availability. Getting rid of the code
- Fix documentation bad github URL

## 1.0.0

### Features

- Added timeout configuration for slow Redmine connections
- Documentation updated
- Changed About image
- Added Enter as default action trigger for buttons
- Code refactoring
- Updated to Groovy 2.4

### Issues

- Redmine project list were not using the configured http client
- Https connections were not handled properly
- Different behavior between key shortcuts and buttons

### Misc

Reported Issue to Taiga when removing a project: https://tree.taiga.io/project/taiga/issue/2616
