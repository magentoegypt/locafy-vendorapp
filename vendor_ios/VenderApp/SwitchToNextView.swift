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

class SwitchToNextView: NSObject {
    
    class func switchToNextView(_ selfRef:UIViewController, vc_identifier:String)
    {
        var selfReference : UIViewController;
//        if(vc_identifier == "ProductDetailsViewController")
//        {
//            selfRef = selfRef as! ProductDetailsViewController;
//            selfRef.tabs
//            
//        }
        
//         /print(selfReference.tabs);
        
        /*
        if(selfRef.tabs["websites"] != nil || selfReference.tabs["categories"] != nil)
        {
            print("switchToNextView");
            let viewcontrollor = selfReference.storyboard?.instantiateViewControllerWithIdentifier("website_Category_ViewController") as! Website_Category_ViewController;
            
            //edit time
            viewcontrollor.isEditCase = isEditCase;
            viewcontrollor.selectedFiltersIds = productSelectedCategories;
            viewcontrollor.selectedWebsitesIds = productSelectedWebsites;
            //edit time
            
            
            viewcontrollor.selectedProductTypeId = selectedProductTypeId;
            viewcontrollor.selectedAttributeSetId = selectedAttributeSetId;
            viewcontrollor.stock = self.stock;
            viewcontrollor.taxes = self.taxes;
            viewcontrollor.tabs = self.tabs;
            viewcontrollor.storeViews = self.storeViews;
            viewcontrollor.websites = self.websites;
            viewcontrollor.categoriesList = self.categoriesList;
            viewcontrollor.attributes = attributes;
            viewcontrollor.product_id = self.product_id;
            viewcontrollor.configSelected = self.configSelected;
            
            let navigation = ced_navigationBarController();
            navigation.setViewControllers([viewcontrollor], animated: true);
            
            self.sideDrawerViewController?.transitionFromMainViewController(navigation, duration: 1, options: UIViewAnimationOptions.CurveLinear, animations: nil, completion: nil);
            
            //self.navigationController?.pushViewController(viewcontrollor, animated: true);
            return;
        }*/

        
        
    }

}
