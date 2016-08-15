/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
    StyleSheet,
    Text,
    ListView,
    View
} from 'react-native';
import Button from 'react-native-button';
import FCM from 'react-native-fcm';

class KahaHaiBoseDK extends Component {
  render() {
    return (
        <View style={styles.container}>
        <LotsOfUsers />
        <Geolocation />
        <App />
        </View>
        );
  }
}

class App extends Component {
  componentDidMount() {
    console.log("initialData:"+JSON.stringify(FCM.initialData))
      FCM.requestPermissions();
    FCM.getFCMToken().then(token => {
      console.log("token:"+token)
      fetch('https://khb.herokuapp.com/register', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
        'Content-Type': 'application/json'
        },
        body: JSON.stringify({
                name: 'Ravi',
        phone: '5307461414',
        fcm_id: token 
              })
        }).then((response) => {
        return response;
      }).then((response) => {
          console.log(response)
        });
    });
    this.notificationUnsubscribe = FCM.on('notification', (data) => {
      console.log('notif:'+JSON.stringify(data))
    });
    this.refreshUnsubscribe = FCM.on('refreshToken', (token) => {
      console.log("token:"+token)
    });

    FCM.subscribeToTopic('/topics/foo-bar');
    FCM.unsubscribeFromTopic('/topics/foo-bar');
  }

  render() {
    return null
  }


  componentWillUnmount() {
    this.refreshUnsubscribe();
    this.notificationUnsubscribe();
  }
}

let ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2})

  class LotsOfUsers extends Component {
    constructor(props) {
      super(props)
        this.state = {
          dataSource: ds
        }
    }

    componentDidMount() {
      fetch('https://khb.herokuapp.com/users?phone=17164319480').then((response) => {
        return response.json()
    }).then((responseJson) => {
      this.setState({
        dataSource: ds.cloneWithRows(responseJson)
      })
    })
    }

    _handlePress(rowData) {
      fetch('https://khb.herokuapp.com/request', {
          method: 'POST',
          headers: {
            'Content-Type': "application/json",
      'Accept': "application/json"
          },
          body: JSON.stringify({
                  from: '17164319480',
          to: rowData.phone
                })
          }).then(response => {
        console.log(response)
      })

    console.log(rowData)
    }

    renderRow(rowData) {
      return (
          <View>
          <Button
          containerStyle={{padding:10, height:45, marginTop: 10, overflow:'hidden', borderRadius:4, backgroundColor: '#dddddd'}}
          onPress={() => this._handlePress(rowData)}
          style={{fontSize: 20, color: 'black'}}>
          {rowData.name} 
          </Button>
          </View>
          ) 
    }

    render() {
      return(
          <View>
          <ListView
          dataSource={this.state.dataSource}
          renderRow={this.renderRow.bind(this)}
          />
          </View>
          )
    }
  }

class Geolocation extends Component {
  state = {
    initialPosition: 'unknown',
    lastPosition: 'unknown'
  }

  watchID: ?number = null;

           componentDidMount() {
             navigator.geolocation.getCurrentPosition((position) => {
               var initialPosition = JSON.stringify(position)
               this.setState({position: initialPosition})
             },
             (error) => alert(error),
             {enableHighAccuracy: true, timeout: 20000, maximumAge: 1000})

             this.watchID = navigator.geolocation.watchPosition((position) => {
               var lastPosition = JSON.stringify(position);
               console.log(lastPosition)
               this.setState({lastPosition})
             })
           }

           componentWillunmount() {
             navigator.geolocation.clearWatch(this.watchID)
           }

           render() {
             return (
                 <View>
                 <Text>
                 {this.state.initialPosition}
                 </Text>
                 <Text>
                 {this.state.lastPosition}
                 </Text>
                 </View>
                 )
           }
}

const styles = StyleSheet.create({
  container: {
               flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: '#F5FCFF',
             },
      welcome: {
                 fontSize: 20,
      textAlign: 'center',
      margin: 10,
               },
      instructions: {
                      textAlign: 'center',
      color: '#333333',
      marginBottom: 5,
                    },
});

AppRegistry.registerComponent('KahaHaiBoseDK', () => KahaHaiBoseDK);
