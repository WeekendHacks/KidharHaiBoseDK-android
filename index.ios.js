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

class KahaHaiBoseDK extends Component {
  render() {
    return (
      <View style={styles.container}>
				<LotsOfUsers />
      </View>
    );
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
		fetch('https://khb.herokuapp.com/test').then((response) => {
			return response.json()
		}).then((responseJson) => {
			this.setState({
				dataSource: ds.cloneWithRows(responseJson.users)
			})
		})
	}

	renderRow(rowData) {
		return (
				<View>
					<Button
    				containerStyle={{padding:10, height:45, marginTop: 10, overflow:'hidden', borderRadius:4, backgroundColor: '#dddddd'}}
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
