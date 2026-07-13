/**
* CedCommerce
*
* NOTICE OF LICENSE
*
* This source file is subject to the End User License Agreement (EULA)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://cedcommerce.com/license-agreement.txt
*
* @category  Ced
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit
import CoreData
import FirebaseCore
import FirebaseMessaging
import IQKeyboardManager

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate,UNUserNotificationCenterDelegate {
    
    var window: UIWindow?
    var fcmToken:String = ""
    @objc func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        //UILabel.appearance().substituteFontName="Roboto-Thin"
       // print("SDK version \(Settings.sdkVersion)")
        FirebaseApp.configure()
        UNUserNotificationCenter.current().delegate = self
        window!.overrideUserInterfaceStyle = .light
      //  ApplicationDelegate.shared.application(application, didFinishLaunchingWithOptions: launchOptions)
        IQKeyboardManager.shared().isEnabled = true
       // IQKeyboardManager.shared().resignOnTouchOutside = true
        if(UIButton.appearance().backgroundColor == UIColor.red || UIButton.appearance().backgroundColor == Ced_CommonVendor.UIColorFromRGB("#ff0000"))
        {
            UIButton.appearance().backgroundColor = UIColor.orange
        }
        URLCache.shared.removeAllCachedResponses()
//        UIView.appearance().semanticContentAttribute = .forceRightToLeft
//        UISearchBar.appearance().semanticContentAttribute = .forceRightToLeft
//        UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).textAlignment = .right
//        UITextField.appearance().textAlignment = .right
        
        let deafaults = UserDefaults.standard
        let value=UserDefaults.standard.value(forKey: "SelectLanguages") as? [String] ?? []
        if(value.count > 0){
            ced_storeVC.selectLangauge = value[0]
        }
        if isFirstLuanch(){
            let story = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = story.instantiateViewController(withIdentifier: "ced_storeVC") as? ced_storeVC
            self.window?.rootViewController = viewControl
            self.window?.makeKeyAndVisible()
        }else{
            load_app()
//            if(deafaults.bool(forKey: "isLogin")){
//                let story = UIStoryboard(name: "Main", bundle: nil)
//                let viewControl = story.instantiateViewController(withIdentifier: "rootafterlogin") as? MKSideDrawerViewController
//                self.window?.rootViewController = viewControl
//                self.window?.makeKeyAndVisible()
//                 Ced_CommonVendor().loadData()
//            }else{
//                let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
//                let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
//                self.window?.rootViewController = viewControl
//                self.window?.makeKeyAndVisible()
//                Ced_CommonVendor().loadData()
//            }
//            Ced_CommonVendor.checkmoduless()
//            Ced_CommonVendor().isRegAvail()
        }
        
//        let story = UIStoryboard(name: "Main", bundle: nil)
//        let viewControl = story.instantiateViewController(withIdentifier: "ced_storeVC") as? ced_storeVC
//        self.window?.rootViewController = viewControl
//        self.window?.makeKeyAndVisible()
         // load_app()
       
        //Ced_CommonVendor().loadModules()
        if #available(iOS 8.0, *) {
            let notificationTypes: UIUserNotificationType = [UIUserNotificationType.alert, UIUserNotificationType.badge, UIUserNotificationType.sound]
            let pushNotificationSettings = UIUserNotificationSettings(types: notificationTypes, categories: nil)
            application.registerUserNotificationSettings(pushNotificationSettings)
            application.registerForRemoteNotifications()
        } else {
            // Fallback on earlier versions
        }
       
        return true
    }
    
    func load_app(){
        let value=UserDefaults.standard.value(forKey: "SelectLanguages") as? [String] ?? []
        if(value.count > 0){
            ced_storeVC.selectLangauge = value[0]
        }
        if (ced_storeVC.selectLangauge == "en"){
            UserDefaults.standard.removeObject(forKey: "AppleLanguages")
            UserDefaults.standard.setValue("eg-en/", forKey: "storeCode")
            UserDefaults.standard.set(["en"], forKey: "AppleLanguages")
            UserDefaults.standard.set(["en"], forKey: "SelectLanguages")
            UserDefaults.standard.set("en", forKey: "i18n_language")
            UserDefaults.standard.synchronize()
            Bundle.setLanguage("en")
            ced_storeVC.selectLangauge = "en"
            //self.view.reloadInputViews()
            UIView.appearance().semanticContentAttribute = .forceLeftToRight
            UISearchBar.appearance().semanticContentAttribute = .forceLeftToRight
            UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).textAlignment = .left
            UITextField.appearance().textAlignment = .left
            UITextView.appearance().textAlignment = .left
        }else {
            UserDefaults.standard.removeObject(forKey: "AppleLanguages")
            UserDefaults.standard.setValue("eg/", forKey: "storeCode")
            UserDefaults.standard.set(["ar"], forKey: "AppleLanguages")
            UserDefaults.standard.set(["ar"], forKey: "SelectLanguages")
            UserDefaults.standard.set("ar", forKey: "i18n_language")
            UserDefaults.standard.synchronize()
            Bundle.setLanguage("ar")
            ced_storeVC.selectLangauge = "ar"
           // UIApplicationMain(CommandLine.argc, CommandLine.unsafeArgv, nil, NSStringFromClass(AppDelegate.self))
            //self.view.reloadInputViews()
            UIView.appearance().semanticContentAttribute = .forceRightToLeft
            UISearchBar.appearance().semanticContentAttribute = .forceRightToLeft
            UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).textAlignment = .right
            UITextField.appearance().textAlignment = .right
            UITextView.appearance().textAlignment = .right
        }
        let deafaults = UserDefaults.standard
        if(deafaults.bool(forKey: "isLogin")){
            let story = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = story.instantiateViewController(withIdentifier: "rootafterlogin") as? MKSideDrawerViewController
            window?.rootViewController = viewControl
            window?.makeKeyAndVisible()
             Ced_CommonVendor().loadData()
        }else{
            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
            let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
            window?.rootViewController = viewControl
            window?.makeKeyAndVisible()
            Ced_CommonVendor().loadData()
        }
        Ced_CommonVendor.checkmoduless()
        Ced_CommonVendor().isRegAvail()
    }
    
    @objc func changeModules()
    {
        
        let deafaults = UserDefaults.standard
        if(deafaults.bool(forKey: "isLogin")){
            let story = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = story.instantiateViewController(withIdentifier: "rootafterlogin") as? MKSideDrawerViewController
            self.window?.rootViewController = viewControl
            self.window?.makeKeyAndVisible()
            Ced_CommonVendor().loadData()
            
        }else{
            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
            let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
            self.window?.rootViewController = viewControl
            self.window?.makeKeyAndVisible()
            Ced_CommonVendor().loadData()
        }
        
    }
    
    func isFirstLuanch()-> Bool{
        if UserDefaults.standard.bool(forKey: "isAppAlreadyLaunchedOnce") {
            return false
        }else{
            
            return true
        }
    }
    
    @objc func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenChars = (deviceToken as NSData).bytes.bindMemory(to: CChar.self, capacity: deviceToken.count)
        var tokenString = ""
        for i in 0..<deviceToken.count {
            tokenString += String(format: "%02.2hhx", arguments: [tokenChars[i]])
        }
        
        Messaging.messaging().token { token, error in
          if let error = error {
            print("Error fetching FCM registration token: \(error)")
          } else if let token = token {
            print("FCM registration token: \(token)")
              self.fcmToken = token
             // self.sendFCMTokentoServer(token)
          }
        }
    }
    
    func sendFCMTokentoServer(){
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendor_id = userData["vendorId"] as! String
        var url = settings.baseUrl
        url += "vendorapi/index/vendordevice"
        let postString = "Token=" + self.fcmToken + "&vendor_id=" + vendor_id + "&type=2"
        var request = URLRequest(url: URL(string:url)!)
        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data, response, error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                return;
            }
        })
        task.resume()
    }
    
    func removeFCMTokentoServer(){
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendor_id = userData["vendorId"] as! String
        var url = settings.baseUrl
        url += "vendorapi/index/removevendordevice"
        let postString = "Token=" + self.fcmToken + "&vendor_id=" + vendor_id + "&type=2"
        var request = URLRequest(url: URL(string:url)!)
        request.httpMethod = "POST"
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.httpAdditionalHeaders = [
            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data, response, error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                return;
            }
        })
        task.resume()
    }
    
    func deleteAccount(){
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendor_id = userData["vendorId"] as! String
        var url = settings.baseUrl
        url += "rest/V1/vendorapi/deletevendor"
        print(url)
        let parameters :[String:Any] = ["vendor_id": vendor_id]
        let bodycomponents :[String:Any] = ["parameters": parameters]
        
      //  let postString = "Token=" + self.fcmToken + "&vendor_id=" + vendor_id + "&type=2"
        var request = URLRequest(url: URL(string:url)!)
        //HTTP Headers
           request.addValue("application/json", forHTTPHeaderField: "Content-Type")
           request.addValue("application/json", forHTTPHeaderField: "Accept")
        request.httpMethod = "POST"
        if let theJSONData = try? JSONSerialization.data(
            withJSONObject: bodycomponents,
            options: []) {
            let theJSONText = String(data: theJSONData,
                                           encoding: .ascii)
                print("JSON string = \(theJSONText!)")
        }
        let postString = "{\"parameters\":{\"vendor_id\":\"149\"}}"
        let jsonData = try? JSONSerialization.data(withJSONObject: bodycomponents)
        request.httpBody = jsonData//postString.data(using: String.Encoding.utf8)
       // let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
       // request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data, response, error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                var responseString = String(data: data!,
                                            encoding: .utf8)
                print("response = \(responseString)")
                return;
            }
        })
        task.resume()
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        // Handle the notification and perform necessary actions
        completionHandler()
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,willPresent notification: UNNotification,withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions)
            -> Void) {
            completionHandler([.alert, .badge, .sound])
        } // add this function
    
    @objc func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print(error)
    }
    @objc func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        print(userInfo)
        
    }
  
    @objc func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }
    
    @objc func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }
    
    @objc func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
        UIApplication.shared.applicationIconBadgeNumber = 0
    }
    
    @objc func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        application.applicationIconBadgeNumber = 0
    }
    
    @objc func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        // Saves changes in the application's managed object context before the application terminates.
        self.saveContext()
    }
    
    // MARK: - Core Data stack
    
    lazy var applicationDocumentsDirectory: URL = {
        // The directory the application uses to store the Core Data store file. This code uses a directory named "com.cedcommerece.venderapp.VenderApp" in the application's documents Application Support directory.
        let urls = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return urls[urls.count-1]
        }()
    
    lazy var managedObjectModel: NSManagedObjectModel = {
        // The managed object model for the application. This property is not optional. It is a fatal error for the application not to be able to find and load its model.
        let modelURL = Bundle.main.url(forResource: "VenderApp", withExtension: "momd")!
        return NSManagedObjectModel(contentsOf: modelURL)!
        }()
    
    lazy var persistentStoreCoordinator: NSPersistentStoreCoordinator = {
        // The persistent store coordinator for the application. This implementation creates and returns a coordinator, having added the store for the application to it. This property is optional since there are legitimate error conditions that could cause the creation of the store to fail.
        // Create the coordinator and store
        let coordinator = NSPersistentStoreCoordinator(managedObjectModel: self.managedObjectModel)
        let url = self.applicationDocumentsDirectory.appendingPathComponent("SingleViewCoreData.sqlite")
        var failureReason = "There was an error creating or loading the application's saved data."
        do {
            try coordinator.addPersistentStore(ofType: NSSQLiteStoreType, configurationName: nil, at: url, options: nil)
        } catch {
            // Report any error we got.
            var dict = [String: AnyObject]()
            dict[NSLocalizedDescriptionKey] = "Failed to initialize the application's saved data" as AnyObject?
            dict[NSLocalizedFailureReasonErrorKey] = failureReason as AnyObject?
            
            dict[NSUnderlyingErrorKey] = error as NSError
            let wrappedError = NSError(domain: "YOUR_ERROR_DOMAIN", code: 9999, userInfo: dict)
            // Replace this with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this @objc function in a shipping application, although it may be useful during development.
            NSLog("Unresolved error \(wrappedError), \(wrappedError.userInfo)")
            abort()
        }
        
        return coordinator
        }()
    
    lazy var managedObjectContext: NSManagedObjectContext = {
        // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.) This property is optional since there are legitimate error conditions that could cause the creation of the context to fail.
        let coordinator = self.persistentStoreCoordinator
        var managedObjectContext = NSManagedObjectContext(concurrencyType: .mainQueueConcurrencyType)
        managedObjectContext.persistentStoreCoordinator = coordinator
        return managedObjectContext
        }()
    
    // MARK: - Core Data Saving support
    
    @objc func saveContext () {
        if managedObjectContext.hasChanges {
            do {
                try managedObjectContext.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // abort() causes the application to generate a crash log and terminate. You should not use this @objc function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                NSLog("Unresolved error \(nserror), \(nserror.userInfo)")
                abort()
            }
        }
    }
    
}

//https://locafy.market/fcm.php
