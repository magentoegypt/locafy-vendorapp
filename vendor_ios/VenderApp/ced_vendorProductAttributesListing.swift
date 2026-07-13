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

class ced_vendorProductAttributesListing: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    
    @IBOutlet weak var attributeListTableView: UITableView!
    @IBOutlet weak var topContainer: UIView!
    @IBOutlet weak var showFilter: UIButton!
    @IBOutlet weak var toplabel: UILabel!
    
    var attributeData = [[String:String]]()
    var Floatbutton = UIButton()
    var applyFilter = false
    var filterString = String()
    var filterPostdata = NSDictionary()
    var page = 1
    var loading = true
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        self.title = "Vendor Attributes".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topContainer.backgroundColor = color
        attributeListTableView.dataSource = self
        attributeListTableView.delegate = self
        // Do any additional setup after loading the view.
        showFilter.addTarget(self, action: #selector(ced_vendorProductAttributesListing.showFilterPopup(_:)), for: UIControl.Event.touchUpInside)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        ced_navigationBarController().addNavButton(self,str:"no".localized)
        self.loadAttributeData(1)
        self.addFloatingbutton()
    }
    
    @objc func addFloatingbutton(){
        
        let bounds = self.view.bounds
        Floatbutton = UIButton(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        let imageview = UIImageView(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        imageview.image = UIImage(named: "add_attribute")
        imageview.layer.cornerRadius = imageview.frame.width/2
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        Floatbutton.backgroundColor = UIColor.clear
        imageview.backgroundColor = color
        imageview.contentMode = .center
        Floatbutton.addTarget(self, action: #selector(ced_vendorProductAttributesListing.AddAttribute(_:)), for: UIControl.Event.touchUpInside)
        self.view.insertSubview(imageview, aboveSubview: self.attributeListTableView)
        self.view.insertSubview(Floatbutton, aboveSubview: self.attributeListTableView)
    }
    
    @objc func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let bounds = attributeListTableView.bounds
        let frame = Floatbutton.frame
        Floatbutton.frame.origin.y = bounds.origin.y + 400
        self.view.bringSubviewToFront(Floatbutton)
        self.Floatbutton.frame = frame;
    }
    
    @objc func AddAttribute(_ sender:UIButton){
        let story = UIStoryboard(name: "ProductAttributes", bundle: nil)
        let View  = story.instantiateViewController(withIdentifier: "addAttrnew") as! ced_addATTrbutenew
        View.edit=false
        self.navigationController?.pushViewController(View, animated: true)
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
        
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return attributeData.count
    }
    
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if attributeData.count > 0 {
        let cellData = attributeData[indexPath.row]
        let cell = attributeListTableView.dequeueReusableCell(withIdentifier: "vendorAttributes") as! ced_attributeListcell
        cell.attriButeCode.text = cellData["attribute_code"]
        cell.attributeLabel.text = cellData["frontend_label"]
        cell.visible.text = cellData["is_visible_on_front"]
        cell.searchable.text = cellData["is_searchable"]
        cell.comparable.text = cellData["is_comparable"]
        cell.scope.text = cellData["is_global"]
        cell.Required.text = cellData["is_required"]
        cell.system.text = cellData["is_user_defined"]
        cell.useinLayered.text = cellData["is_filterable"]
       cell.delete_atr_cell.tag=44+indexPath.row
            cell.delete_atr_cell.setTitle("Delete".localized, for: .normal)
            cell.delete_atr_cell.addTarget(self, action: #selector(delete_attribute(_:)), for: UIControl.Event.touchUpInside)
            return cell
        }else{
            return UITableViewCell()
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 380
    }
    
    @objc func delete_attribute(_ sender:UIButton)
    {
        var p=sender.tag
        p=p-44
        //let attributeid: String
        
            let dat = attributeData[p]
            let attributeid=dat["attribute_id"]!
            print("BEFORESENDINGATTRIBUTEID\(dat["attribute_id"])")
        
        let refreshAlert = UIAlertController(title: "Delete".localized, message: "Are you sure you want to delete this attribute?".localized, preferredStyle: UIAlertController.Style.alert)
        
        refreshAlert.addAction(UIAlertAction(title: "Ok".localized, style: .default, handler: { (action: UIAlertAction!) in
            let url = "vproductattributeapi/attribute/delete"
            let userData = self.defaults.object(forKey: "userInfoDict") as! NSDictionary
            //print(userData)
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
            postString  += "&id=" + attributeid
            print(postString)
            self.sendRequest(url: url, parameters: postString)
            
        }))
        refreshAlert.addAction(UIAlertAction(title: "Cancel".localized, style: .cancel, handler: { (action: UIAlertAction!) in
            print("Handle Cancel Logic here")
        }))
        
        present(refreshAlert, animated: true, completion: nil)
    }
    @objc func loadAttributeData(_ page:Int){
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        let baseUrl = "vproductattributeapi/index/getVProductAttribute/page/\(page)"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if(applyFilter){
            postString += "&filter=" + filterString//.addingPercentEncoding(withAllowedCharacters: CharacterSet.alphanumerics)!
        }
        self.sendRequest(url: baseUrl, parameters: postString)
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl == "vproductattributeapi/attribute/delete")
            {
                
                guard let json = try? JSON(data: data) else {return}
                print("jsonDelete---\(json)")
                //print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parseData(json)
                    let refreshAlert = UIAlertController(title: "Successfull".localized, message: json["data"]["message"].stringValue, preferredStyle: UIAlertController.Style.alert)
                    refreshAlert.addAction(UIAlertAction(title: "Ok".localized, style: .default, handler: { (action: UIAlertAction!) in
                        self.attributeListTableView.reloadData()
                    }))
                    present(refreshAlert, animated: true, completion: nil)
                    
                }else{
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: json["data"]["message"].stringValue)
                }
                
            }
            else{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.parseData(json)
                }else{
                    let refreshAlert = UIAlertController(title: "Error".localized, message: json["data"]["message"].stringValue, preferredStyle: UIAlertController.Style.alert)
                    
                    refreshAlert.addAction(UIAlertAction(title: "Ok".localized, style: .default, handler: { (action: UIAlertAction!) in
                    }))
                  //  present(refreshAlert, animated: true, completion: nil)
                    self.attributeListTableView.setEmptyMessage(json["data"]["message"].stringValue)
                    return
                }
                self.attributeListTableView.reloadData()
            }
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if attributeData.count > 0 {
         let dat = attributeData[indexPath.row]
        let story = UIStoryboard(name: "ProductAttributes", bundle: nil)
        let View  = story.instantiateViewController(withIdentifier: "addAttrnew") as! ced_addATTrbutenew
        View.edit = true
            print("BEFORESENDINGATTRIBUTEID\(dat)")
        View.attributeId = dat["attribute_id"]!
        self.navigationController?.pushViewController(View, animated: true)
        }
        
    }
    
    
    func parseData(_ json:JSON){
        
        
        
        
        for result in json["data"]["vendor_attributes"].arrayValue{
            var data = [String:String]()
            data["is_searchable"] = result["is_searchable"].stringValue
            data["is_configurable"] = result["is_configurable"].stringValue
            data["is_comparable"] = result["is_comparable"].stringValue
            data["is_global"] = result["is_global"].stringValue
            data["is_required"]     = result["is_required"].stringValue
            data["is_user_defined"] = result["is_user_defined"].stringValue
            data["attribute_id"] = result["attribute_id"].stringValue
            data["is_visible_on_front"] = result["is_visible_on_front"].stringValue
            data["is_filterable"] = result["is_filterable"].stringValue
            data["frontend_label"] = result["frontend_label"].stringValue
            data["attribute_code"] = result["attribute_code"].stringValue
            self.attributeData.append(data)
            
        }
        return
    }
    
    
    
    @objc func showFilterPopup(_ sender:UIButton){
        /* background view */
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_attrfilterView();
        self.addgesturesTohideView(self.view)
        if(filterPostdata.count != 0){
            filterbyView.attributelabel?.text = filterPostdata.object(forKey: "frontend_label") as? String
            filterbyView.scope?.setTitle(filterPostdata.object(forKey: "is_global") as? String, for: UIControl.State())
            
            filterbyView.searchable.setTitle(filterPostdata.object(forKey: "is_searchable") as? String, for: UIControl.State())
            
            filterbyView.comparable.setTitle(filterPostdata.object(forKey: "is_comparable") as? String, for: UIControl.State())
            
        filterbyView.required?.setTitle(filterPostdata.object(forKey: "is_required") as? String, for: UIControl.State())
            
            filterbyView.useinlayered?.setTitle(filterPostdata.object(forKey: "is_user_defined") as? String, for: UIControl.State())
            
            filterbyView.atributecode?.text = filterPostdata.object(forKey: "attribute_code") as? String
            
            filterbyView.visible?.setTitle(filterPostdata.object(forKey: "is_visible") as? String, for: UIControl.State())
        }
        filterbyView.Filter.addTarget(self, action: #selector(ced_vendorProductAttributesListing.applyFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.resetFilter.addTarget(self, action: #selector(ced_vendorProductAttributesListing.resetFilters(_:)), for: UIControl.Event.touchUpInside)
        filterbyView.tag = 181;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 25, y: 25, width: self.view.frame.width - 50, height: 470)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
    }
    
    @objc func applyFilters(_ sender:UIButton){
        
        attributeData = [[String:String]]()
        self.applyFilter = true
        let view = self.view.viewWithTag(181) as! ced_attrfilterView
        var attributecode = ""
        if let attrcode = view.atributecode.text {
            attributecode =  attrcode
        }
        var attributeLabel  = ""
        if let attrlbl = view.attributelabel.text {
            attributeLabel = attrlbl
        }
        
        var required = ""
        if let req = view.required.titleLabel?.text {
            if req != "Select" {
                required = req
            }
        }
        
        var system  = ""
        if let sys = view.system.titleLabel?.text {
            if sys != "Select" {
                system = sys
            }
        }
        
        var comparable = ""
        if let comp = view.comparable.titleLabel?.text{
             if comp != "Select" {
            comparable = comp
            }
        }
        
        
        var visible = ""
        if let vis = view.visible.titleLabel?.text {
              if vis != "Select" {
            visible = vis
            }
        }
        
        var scope  = ""
        if let scop = view.scope.titleLabel?.text {
              if scop != "Select" {
            scope = scop
            }
        }
        var search  = ""
        if let searchable = view.searchable.titleLabel?.text {
            if searchable != "Select" {
            search = searchable
            }
        }
        
        var usedinlayered = ""
        if let usein = view.useinlayered.titleLabel?.text {
             if usein != "Select" {
            usedinlayered = usein
            }
        }
        let postdata = ["attribute_code":attributecode,"frontend_label":attributeLabel,"is_required":required,"is_global":scope,"is_visible":visible,"is_comparable":comparable,"is_user_defined":usedinlayered,"is_searchable":search]
        
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: postdata ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            filterString = NSString(data: JSONData,
                encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        self.filterPostdata = postdata as NSDictionary
        print(filterPostdata)
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadAttributeData(1)
        
    }
    
    @objc func resetFilters(_ sender:UIButton){
        self.filterPostdata = NSDictionary()
        self.applyFilter = false
        self.filterString = String()
        attributeData = [[String:String]]()
        self.view.viewWithTag(151)?.removeFromSuperview()
        self.loadAttributeData(1)
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ced_vendorProductAttributesListing.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorProductAttributesListing.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorProductAttributesListing.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorProductAttributesListing.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ced_vendorProductAttributesListing.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: attributeListTableView))
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? ced_attrfilterView {
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                page += 1
                loadAttributeData(page);
            }
        }
    }
}


