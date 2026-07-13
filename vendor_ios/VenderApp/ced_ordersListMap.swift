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
import MapKit

class ced_ordersListMap: ced_VendorBaseClass {
    @IBOutlet weak var mapView: MKMapView!
    var mapData = [[String:String]]()

    override func viewDidLoad() {
        super.viewDidLoad()
       
    ced_navigationBarController().addNavButton(self,str:"no")
        print("---mapData----")
        print(mapData)
        
        for data in mapData{
            if let countrycode = data["country_code"]
            {
                let geocoder: CLGeocoder = CLGeocoder()
                geocoder.geocodeAddressString(countrycode,completionHandler: {(placemarks: [CLPlacemark]?, error: Error?) -> Void in
                    if let placemarks = placemarks{
                        if (placemarks.count > 0) {
                            let topResult: CLPlacemark = (placemarks[0])
                            let placemark: MKPlacemark = MKPlacemark(placemark: topResult)
                            
                            let array = ["lat":(placemark.location?.coordinate.latitude.description)!,"longi":(placemark.location?.coordinate.longitude.description)!]
                            self.addToMap(array: array, data: data)
                            
                            var region: MKCoordinateRegion = self.mapView.region
                            
                            //region.center.latitude = (placemark.location?.coordinate.latitude)!
                            //region.center.longitude = (placemark.location?.coordinate.longitude)!
                            
                            region.span = MKCoordinateSpan.init(latitudeDelta: 0.5, longitudeDelta: 0.5)
                            
                            //self.mapView.setRegion(region, animated: true)
                            self.mapView.addAnnotation(placemark)
                        }
                    }
                    
                })
                //let array =  Ced_CommonVendor.getDataformjson(countrycode!) as! [String:String]
                
            }
        }
    }
    
    @objc func addToMap(array: [String:String], data: [String:String])
    {
        let point = MKPointAnnotation()
        let coordinat = CLLocationCoordinate2DMake(Double(array["lat"]!)!, Double(array["longi"]!)!)
        point.coordinate = coordinat
        let ar = CLGeocoder()
        ar.reverseGeocodeLocation(CLLocation(latitude: Double(array["lat"]!)!, longitude: Double(array["longi"]!)!), completionHandler: {
            placemark,error in
            let placemark1 = placemark?.first
            let order = data["total"]
            let amount = data["ammount"]
            point.title = placemark1!.administrativeArea
            point.subtitle = "Orders Placed :".localized + order!+"\n"+"Total Amount :".localized + "\(amount!)"
            self.mapView.addAnnotation(point)
            //self.mapView.showAnnotations([point], animated: true)
        })
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
       
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
