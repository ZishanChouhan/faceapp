/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import type {PropsWithChildren} from 'react';
import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  useColorScheme,
  View,
  NativeModules,
  PermissionsAndroid
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

type SectionProps = PropsWithChildren<{
  title: string;
}>;


function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const { FacemeshModule } = NativeModules 

  const openCamera = () => {
    PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.CAMERA).then(res => {
      console.log("res", res);
      if(res){
        FacemeshModule.openCamera()
      }else{
        PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA).then(response => {
          console.log("response", response)
          if(response == PermissionsAndroid.RESULTS.GRANTED){
            FacemeshModule.openCamera()
          }
      }).catch(err => console.log("err", err))
    }

    }).catch(err => console.log("err", err))
  }

  return (
    <SafeAreaView style={{
      backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
      flex: 1,
      justifyContent: "center",
      alignItems: "center"
    }}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={isDarkMode ? Colors.darker : Colors.lighter}
      />
      <TouchableOpacity onPress={openCamera} style={{backgroundColor: "#000", }}>
        <Text style={{color: "#fff", paddingVertical: 10, paddingHorizontal: 20}}>open</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
