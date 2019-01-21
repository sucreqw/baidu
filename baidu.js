var hexDigits = "0123456789ABCDEF";
var enc;
var pos=157;
function toarray(s){
    enc=new Array()
//enc[0]=48;
    var p='';
    var t='';
    var ret='';
    for(i=0;i<s.length();i++){
        p=s.substr(i,2);
        i++;
        t=parseInt(p,16);
        enc.push(t);

    }
    return toHexString();
    return enc.toString();
}

function toHexString () {

    return hexDump(25,157, !0)

}

function hexByte (t) {
    return hexDigits.charAt(t >> 4 & 15) + hexDigits.charAt(15 & t)
}

function hexDump (t, e, i) {
    for (var r = "", s = t; e > s; ++s)
        if (r +=hexByte(this.get(s)),
        i !== !0)
            switch (15 & s) {
                case 7:
                    r += "  ";
                    break;
                case 15:
                    r += "\n";
                    break;
                default:
                    r += " "
            }
    return r
}

function get (e) {
    if (e === undefined && (e = pos++),
    e >= this.enc.length)
        throw "Requesting byte offset " + e + " on a stream of length " + this.enc.length;
    //enc=公钥的十六进制
    return this.enc[e]
}