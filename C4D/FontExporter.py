import c4d
import os

from c4d import gui
from c4d import documents
from c4d import utils

ch_up = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
ch_lo = "abcdefghijklmnopqrstuvwxyz"
ch_num = "0123456789"
ch_sym = ".,-#+/'%@"
characters = ch_up + ch_lo + ch_num + ch_sym
angle_deg = 30
prefix = "char"
suffix = "Q"

def main():
    doc = documents.GetActiveDocument()
    angle_rad = utils.Rad(angle_deg)
    fontbc = gui.FontDialog()
    font = c4d.FontData()
    font.SetFont(fontbc)
    i = 0
    max_height = 0;
    upper_case_len = len(ch_up) 
    for c in characters:
        text = c4d.BaseObject(c4d.Osplinetext)
        text[c4d.PRIM_TEXT_TEXT] = c
        text[c4d.PRIM_TEXT_FONT] = font
        text[c4d.PRIM_TEXT_ALIGN] = c4d.PRIM_TEXT_ALIGN_LEFT
        text[c4d.PRIM_TEXT_HEIGHT] = 100
        text[c4d.SPLINEOBJECT_ANGLE] = angle_rad
        loft = c4d.BaseObject(c4d.Oloft)
        loft.SetName(prefix + str(i) + suffix)
        doc.InsertObject(loft)
        text.InsertUnder(loft)
        doc.SetActiveObject(loft)
        c4d.CallCommand(12236)
        c_height = loft.GetMp().y + loft.GetRad().y
        if i < upper_case_len and c_height > max_height:
            max_height = c_height
        i += 1
        tags = loft.GetTags()
        for tag in tags:
            t = tag.GetType()
            if t == c4d.Tuvw or t == c4d.Tpolygonselection or t == c4d.Tphong :
                tag.Remove()
    c4d.EventAdd()
    objs = doc.GetObjects()
    s = 100 / max_height
    v = c4d.Vector(s, s, 1)
    for obj in objs:
        obj.SetRelScale(v)
    path = c4d.storage.SaveDialog(c4d.FILESELECTTYPE_ANYTHING, "FontExporter", "", doc.GetDocumentPath())
    c4d.documents.SaveDocument(doc, path, c4d.SAVEDOCUMENTFLAGS_DIALOGSALLOWED, c4d.FORMAT_OBJEXPORT)
    for obj in objs:
        obj.Remove()
    f = open(path,'r')
    filedata = f.read()
    f.close()
    for c in range(0,i):
        filedata = filedata.replace(prefix + str(c) + suffix, characters[c])
    f = open(path,'w')
    f.write(filedata)
    f.close()
    gui.MessageDialog(str(i) + " characters have been succesfully exported")

if __name__=='__main__':
    main()
    while gui.QuestionDialog("Do you want to export another font?"):
        main()