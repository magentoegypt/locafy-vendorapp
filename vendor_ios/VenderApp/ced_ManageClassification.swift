    //
    //  ced_ManageClassification.swift
    //  VenderApp
    //
    //  Created by cedcoss on 4/3/21.
    //  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
    //

    import UIKit

    class ced_ManageClassification: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource {
       
        var jsondat = JSON()
        var filterbaseview : UIView!
        var filterview : RatingFilterView!
        var datePickerView = UIDatePicker()
        var globalTextFieldTag = 0;
        var currentPage = 1;
        var loading = true;
        @IBOutlet weak var headerlbl: UILabel!
        @IBOutlet weak var filterbttn: UIButton!
        
        @IBOutlet weak var maintable: UITableView!
       
        override func viewDidLoad() {
            super.viewDidLoad()
            headerlbl.setThemeColor()
            self.maintable.delegate = self
            self.maintable.dataSource = self
            maintable.separatorStyle = .none
            filterbttn.addTarget(self, action: #selector(filterbttnpressed(_:)), for: .touchUpInside)
            datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
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
             sendurlrequest(url: "rest//V1/vproductreviewapi/getRatingList", para: param)
        }
        
        override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
            
            guard let json = try? JSON(data: data!) else {return}
            // decoding here
             print(json)
            if json[0]["vendor_data"]["status"].stringValue == "true" {
                self.jsondat = json
                print(  self.jsondat[0]["vendor_data"]["rating_list"].count)
            }
            else if json[0]["vendor_data"]["status"].stringValue == "false" && currentPage == 1{
                //self.view.makeToast(json[0]["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                self.maintable.setEmptyMessage(json[0]["vendor_data"]["message"].stringValue)
                return;
            }
            else if json[0]["vendor_data"]["status"].stringValue == "false" {
                loading = false
            }
            
                self.maintable.reloadData()
        }
        
      
         
             
        @objc func filterbttnpressed(_ sender:UIButton)
         {
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
         
           filterview = RatingFilterView()
        
         
         filterview.bttnapply.addTarget(self, action: #selector(applybttnpressed(_:)), for: .touchUpInside)
         filterview.bttnclear.addTarget(self, action: #selector(clearbttnpressed(_:)), for: .touchUpInside)
       
         
         filterbaseview.addSubview(filterview)
         filterview.translatesAutoresizingMaskIntoConstraints=false
         filterview.topAnchor.constraint(equalTo: filterbaseview.topAnchor , constant: 200).isActive=true
         filterview.leadingAnchor.constraint(equalTo: filterbaseview.leadingAnchor , constant: 30).isActive=true
         filterview.trailingAnchor.constraint(equalTo: filterbaseview.trailingAnchor ,constant: -30).isActive=true
         filterview.heightAnchor.constraint(equalTo: filterbaseview.heightAnchor ,multiplier: 0.3).isActive=true
         
         }
         
         @objc func clearbttnpressed(_ sender : UIButton)
         {
             view.viewWithTag(567)?.removeFromSuperview()
            filter = ""
            currentPage = 1;

            self.sendDataRequest()
         }
         var filter = String()
         @objc func applybttnpressed(_ sender:UIButton)
         {
             
             let txtid = filterview.id.text
             let txtclassification = filterview.classification.text
             let txtordering = filterview.ordering.text
             let txtisactive = filterview.isactive.text
            
             
             filter = "";
             filter += "{";
             filter += "\""+"rating_id"+"\":\""+txtid!+"\",";
             filter += "\""+"rating_code"+"\":\""+txtclassification!+"\",";
             filter += "\""+"sort_order"+"\":\""+txtordering!+"\",";
             filter += "\""+"is_active"+"\":\""+txtisactive!+"\"}";
              
             
             
             print(filter)
            currentPage = 1;
            self.sendDataRequest()
            filterbaseview.removeFromSuperview()
         }

         @objc func onSelectbackgroundview (_ sender:UITapGestureRecognizer)
         {
             filterbaseview.removeFromSuperview()
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
            return jsondat[0]["vendor_data"]["rating_list"].count
            
        }
        
        func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
            
            let cell = maintable.dequeueReusableCell(withIdentifier: "classificationcell", for: indexPath) as! ced_ManageClassificationCell
            cell.dat = jsondat
            cell.index = indexPath.row
            cell.populatedata()
            return cell
            
        }
        
        func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
            return 100
        }
        
        func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
            
            let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "cedsinglereview") as! ced_SingleRatingPage
            vc.index = jsondat[0]["vendor_data"]["rating_list"][indexPath.row]["rating_id"].intValue
            print(indexPath.row)
            vc.parsedata()
            navigationController?.pushViewController(vc, animated: true)
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
