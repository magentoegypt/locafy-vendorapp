//
//  ced_ManageReviews.swift
//  VenderApp
//
//  Created by cedcoss on 4/3/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit


class ced_ManageReviews: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource  {
   

    @IBOutlet weak var filterbttn: UIButton!
    @IBOutlet weak var Toplabl: UILabel!
    var filterbaseview : UIView!
    var filterview : ReviewFilterview!
    @IBOutlet weak var maintable: UITableView!
    var review_list = [JSON]()
    var datePickerView = UIDatePicker()
    var globalTextFieldTag = 0;
    var filter = String()
    var currentPage = 1;
    var loading = true;
    var statusFilterDict = [String:String]()
    var statusFilterDatasource = [String]()
    var typeFilterDict = [String:String]()
    var typerFilterDatasource = [String]()
    var storeDict = [String:String]()
    var storeArray = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad() 
       
       // sendRequestWithData(url: "vproductreviewapi/getReviewList", params: param)
        maintable.delegate = self
        maintable.dataSource = self
        Toplabl.setThemeColor()
        if(ced_storeVC.selectLangauge == "ar"){
            Toplabl.textAlignment = .right
        }
        filterbttn.addTarget(self, action: #selector(filterbttnpressed(_:)), for: .touchUpInside)
        self.sendDataRequest()
        
        // Do any additional setup after loading the view.
    }
    
    
    func sendDataRequest() {
        var param = Dictionary<String,String>()
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        param["page"] = "\(currentPage)"
        param["vendor_id"] = vendorId
        if filter != "" {
            param["filter"] = filter
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
     //   let param =  ["page":"\(currentPage)","vendor_id" : vendorId]
        sendurlrequest(url: "rest//V1/vproductreviewapi/getReviewList", para: param)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let json = try? JSON(data: data!) else {return}
        print(json)
        if json[0]["vendor_data"]["status"].stringValue == "true" {
            if currentPage == 1{
                self.review_list = json[0]["vendor_data"]["review_list"].arrayValue
                var tempStatus = [String]()
                var tempType = [String]()
                var tempStore = [String]()
                
                for (itm,val) in json[0]["vendor_data"]["status_filter"].dictionaryValue{
                    self.statusFilterDict[itm] = val.stringValue
                    tempStatus.append(val.stringValue)
                }
                for (itm,val) in json[0]["vendor_data"]["type"].dictionaryValue{
                    self.typeFilterDict[itm] = val.stringValue
                    tempType.append(val.stringValue)
                }
                for (itm,val) in json[0]["vendor_data"]["stores"].dictionaryValue{
                    self.storeDict[itm] = val.stringValue
                    tempStore.append(val.stringValue)
                }
                self.statusFilterDatasource = tempStatus
                self.typerFilterDatasource = tempType
                self.storeArray = tempStore
                
            }else{
                json[0]["vendor_data"]["review_list"].arrayValue.forEach{ review in
                    self.review_list.append(review)
                }
            }
        }
       
        if json[0]["vendor_data"]["status"].stringValue == "false" {
            if currentPage == 1{
               // self.view.makeToast(json[0]["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                self.maintable.setEmptyMessage(json[0]["vendor_data"]["message"].stringValue)
            }
            loading = false;
            return
        }
        maintable.restore()
        maintable.reloadData()
    }
    
   @objc func filterbttnpressed(_ sender:UIButton){
        filterbaseview = UIView()
        filterbaseview.tag = 567
        filterbaseview.backgroundColor = .lightText
        view.addSubview(filterbaseview)
        filterbaseview.translatesAutoresizingMaskIntoConstraints=false
        filterbaseview.topAnchor.constraint(equalTo: view.topAnchor).isActive=true
        filterbaseview.leadingAnchor.constraint(equalTo: view.leadingAnchor).isActive=true
        filterbaseview.trailingAnchor.constraint(equalTo: view.trailingAnchor).isActive=true
        filterbaseview.heightAnchor.constraint(equalTo: view.heightAnchor).isActive=true
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.onSelectbackgroundview(_:)))
        
        filterbaseview.addGestureRecognizer(tap)
        
        filterview = ReviewFilterview()
        
        filterview.bttnFrom.addTarget(self, action: #selector(frombttnpressed(_:)), for: .touchUpInside)
        filterview.bttnuntil.addTarget(self, action: #selector(frombttnpressed(_:)), for: .touchUpInside)
        filterview.bttnsituation.addTarget(self, action: #selector(situationpressed(_:)), for: .touchUpInside)
        filterview.bttnvisibility.addTarget(self, action: #selector(visibiltybttnpressed(_:)), for: .touchUpInside)
        filterview.bttntype.addTarget(self, action: #selector(typebttnpressed(_:)), for: .touchUpInside)
        filterview.bttapply.addTarget(self, action: #selector(applybttnpressed(_:)), for: .touchUpInside)
        filterview.bttnclear.addTarget(self, action: #selector(clearbttnpressed(_:)), for: .touchUpInside)
        //filterview.bttnEdit.addTarget(self, action:  #selector(actionbttnpressed(_:)), for: .touchUpInside)
        filterview.bttnFrom.tag = 123
        filterview.bttnuntil.tag = 321
        filterbaseview.addSubview(filterview)
        filterview.translatesAutoresizingMaskIntoConstraints=false
        filterview.topAnchor.constraint(equalTo: filterbaseview.topAnchor , constant: 200).isActive=true
        filterview.leadingAnchor.constraint(equalTo: filterbaseview.leadingAnchor , constant: 30).isActive=true
        filterview.trailingAnchor.constraint(equalTo: filterbaseview.trailingAnchor ,constant: -30).isActive=true
        filterview.heightAnchor.constraint(equalTo: filterbaseview.heightAnchor ,multiplier: 0.5).isActive=true
        
    }
    
    @objc func clearbttnpressed(_ sender : UIButton){
        currentPage = 1
        view.viewWithTag(567)?.removeFromSuperview()
        self.review_list = []
        maintable.reloadData()
        loading = true
        filter = ""
        self.sendDataRequest()
    }
    
    @objc func applybttnpressed(_ sender:UIButton){
        currentPage = 1
        let txtid = filterview.id.text
        let txtfrom = filterview.bttnFrom.titleLabel?.text == "From".localized ? "" :  filterview.bttnFrom.titleLabel?.text
        let txtto = filterview.bttnuntil.titleLabel?.text == "To".localized ? "" :  filterview.bttnuntil.titleLabel?.text
        let txtsituation = filterview.bttnsituation.titleLabel?.text == "Please Select ".localized ? "" : filterview.bttnsituation.titleLabel?.text
        let txttitle = filterview.title.text
        let txtsurname = filterview.surname.text
        let txtevolution = filterview.evaluation.text
        let txtvisibilty = filterview.bttnvisibility.titleLabel?.text == "Please Select ".localized ? "" : filterview.bttnvisibility.titleLabel?.text
        let tcttype = filterview.bttntype.titleLabel?.text == "Please Select ".localized ? "" : filterview.bttntype.titleLabel?.text
        let txtproduct = filterview.product.text
        let txtcode = filterview.code.text
        
        
        let status = statusFilterDict.allKeysForValue(txtsituation ?? "").first ?? ""
        let type = typeFilterDict.allKeysForValue(tcttype ?? "").first ?? ""
        let store = storeDict.allKeysForValue(txtvisibilty ?? "").first ?? ""
        
        filter = "";
        filter += "{";
        filter += "\""+"review_id"+"\":\""+txtid!+"\",";
        filter += "\""+"title"+"\":\""+txttitle!+"\",";
        filter += "\""+"nickname"+"\":\""+txtsurname!+"\",";
        filter += "\""+"detail"+"\":\""+txtevolution!+"\",";
        filter += "\""+"store_id"+"\":\""+store+"\",";
        filter += "\""+"type"+"\":\""+type+"\",";
        filter += "\""+"name"+"\":\""+txtproduct!+"\",";
        filter += "\""+"sku"+"\":\""+txtcode!+"\",";
        filter += "\""+"created_at"+"\":{\"from\":\""+txtfrom!+"\",\"to\":\""+txtto!+"\"}";
        filter += ",";
        filter += "\""+"status"+"\":\""+status+"\"}";
        print(filter)
        
        self.review_list = []
        maintable.reloadData()
        loading = true
        
        self.sendDataRequest()
        filterbaseview.removeFromSuperview()
    }
      
    @objc func onSelectbackgroundview (_ sender:UITapGestureRecognizer){
        filterbaseview.removeFromSuperview()
    }
    
    @objc func frombttnpressed(_ sender:UIButton)
    {
         
        /* background view */
        let bgCView = UIView();
        bgCView.tag=121;
        bgCView.frame = view.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: view.frame.width-70,height: CGFloat(230));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,
                                     y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
   
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
               // let date = dateFormatter.date(from: isodate!)!
                let calendar = Calendar.current
        let components = calendar.dateComponents([.year, .month, .day, .hour], from: Date())
                let finalDate = calendar.date(from:components)
//                datePickerView.minimumDate = finalDate

        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
       
        filterbaseview .addSubview(bgCView);
        bgCView.addSubview(tempInrView);
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.tag=141;
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(tempInrView.frame.height - 40), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        twoButtonView.cancelButton.tag = sender.tag
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.cancelButton.backgroundColor = colorRed;
        
        twoButtonView.doneButton.tag = sender.tag
        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        
        
    }
    @objc func cancelDatePickerView(_ sender:UIButton){
        if sender.tag == 123
        {
            filterview.bttnFrom.setTitle("From".localized, for: .normal)}
        else if sender.tag == 321
        {filterview.bttnuntil.setTitle("To".localized, for: .normal)}
        
        filterbaseview.viewWithTag(121)?.removeFromSuperview();
    }
    
    
    @objc func pickDateFromDatePickerView(_ sender:UIButton)
    {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
       
            dateFormatter.dateFormat = "yyyy-MM-dd";
        
        if sender.tag == 123
        {filterview.bttnFrom.setTitle(dateFormatter.string(from: datePickerView.date), for: .normal)}
        else if sender.tag == 321
        {filterview.bttnuntil.setTitle(dateFormatter.string(from: datePickerView.date), for: .normal)}
        
          //  textField.resignFirstResponder();
         
        filterbaseview.viewWithTag(121)?.removeFromSuperview();
    }
    
    @objc func situationpressed(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = statusFilterDatasource
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)

        if dropDown.isHidden {
        _ = dropDown.show()
        } else {
        dropDown.hide();
        }
    }
    
    @objc func visibiltybttnpressed(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = storeArray
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)

        if dropDown.isHidden {
        _ = dropDown.show()
        } else {
        dropDown.hide();
        }
    }
    
    @objc func typebttnpressed(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = typerFilterDatasource
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)

        if dropDown.isHidden {
        _ = dropDown.show()
        } else {
        dropDown.hide();
        }
    }
    
    @objc func actionbttnpressed(_ sender:UIButton)
    {
        self.view.makeToast("Inside Actionbttn")
        
        let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "SingleReviewPage") as! ced_SingleReview
        vc.index = review_list[sender.tag]["review_id"].stringValue
        
        vc.defaultstatusid = self.statusFilterDict
        print(index)
         
        navigationController?.pushViewController(vc, animated: true)
        
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return review_list.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cel = maintable.dequeueReusableCell(withIdentifier: "reviewcell", for: indexPath) as! ced_Managereviewcell
        cel.data = review_list[indexPath.row]
        cel.storeDict = self.storeDict
        cel.bttnEdit.tag = indexPath.row
        cel.bttnEdit.addTarget(self, action: #selector(actionbttnpressed(_:)), for: .touchUpInside)
        cel.populatedata()
        cel.bttnEdit.setThemeColor()
        cel.bttnEdit.setTitleColor(UIColor.white, for: .normal)
        return cel
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
     return 350
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.currentPage += 1;
                self.sendDataRequest()

            }
        }
    }
    
    
    
}
